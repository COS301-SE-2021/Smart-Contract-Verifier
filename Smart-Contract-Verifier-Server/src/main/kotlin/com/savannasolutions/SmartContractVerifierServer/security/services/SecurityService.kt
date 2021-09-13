package com.savannasolutions.SmartContractVerifierServer.security.services

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.security.configuration.SecurityConfig
import com.savannasolutions.SmartContractVerifierServer.security.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.security.requests.LoginRequest
import com.savannasolutions.SmartContractVerifierServer.security.requests.UserExistsRequest
import com.savannasolutions.SmartContractVerifierServer.security.responses.AddUserResponse
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.security.responses.GetNonceResponse
import com.savannasolutions.SmartContractVerifierServer.security.responses.LoginResponse
import com.savannasolutions.SmartContractVerifierServer.security.responses.UserExistsResponse
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.komputing.khash.keccak.Keccak
import org.komputing.khash.keccak.Keccak.digest
import org.komputing.khash.keccak.KeccakParameter
import org.springframework.stereotype.Service
import org.web3j.crypto.ECDSASignature
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@Service
class SecurityService(val userRepository: UserRepository,
                      val securityConfig: SecurityConfig
) {

    fun getNonce(userId: String): GetNonceResponse {
        if (userRepository.existsById(userId)) {
            val user = userRepository.getById(userId)
            val nonce = ThreadLocalRandom.current().nextLong(1000000000, 9999999999)
            user.nonce = nonce
            userRepository.save(user)
            return GetNonceResponse(nonce, ResponseStatus.SUCCESSFUL)
        }
        return GetNonceResponse(0, ResponseStatus.FAILED)
    }

    fun addUser(addUserRequest: AddUserRequest): ApiResponse<Objects> {
        val wID = addUserRequest.WalletID
        if(userRepository.existsById(wID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "User exists in database")

        val user = User(publicWalletID = addUserRequest.WalletID, alias = addUserRequest.Alias)

        userRepository.save(user)

        return  ApiResponse(status = ResponseStatus.SUCCESSFUL)
    }

    fun login(userId: String, loginRequest: LoginRequest): LoginResponse {
        var match = false
        val prefix = "\u0019Ethereum Signed Message:\n10"
        if(userRepository.existsById(userId)){
            val nonce = userRepository.getById(userId).nonce.toString()
            val message = digest((prefix + nonce).toByteArray(), KeccakParameter.KECCAK_256)
            val signatureBytes = Numeric.hexStringToByteArray(loginRequest.signedNonce)

            val v = signatureBytes[64]
            if(v < 27)
                v.plus(27)
            val signatureData = Sign.SignatureData(v,
                signatureBytes.copyOfRange(0, 32),
                signatureBytes.copyOfRange(32, 64),)
            //----------------------------------------------------------------------------------------------------------
            var recoveredAddress = ""
            for(i in 0..3){
                val publicKey = Sign.recoverFromSignature(
                    i,
                    ECDSASignature(
                        BigInteger(1, signatureData.r),
                        BigInteger(1, signatureData.s)),
                    message)

                if(publicKey != null){
                    recoveredAddress = "0x" + org.web3j.crypto.Keys.getAddress(publicKey)
                    if(recoveredAddress == userId.lowercase()){
                        match = true
                        break
                    }
                }
            }

            if(match){
                //val secretKey = Keys.hmacShaKeyFor(securityConfig.secretKey.encodeToByteArray())
                val jwtToken = Jwts.builder().setSubject(userId).setExpiration(Date(System.currentTimeMillis() + 10800000)).compact()
                val newNonce = ThreadLocalRandom.current().nextLong(1000000000, 9999999999)
                val user = userRepository.getById(userId)
                user.nonce = newNonce
                userRepository.save(user)
                return LoginResponse(ResponseStatus.SUCCESSFUL, jwtToken)
            }
        }
        return LoginResponse(ResponseStatus.FAILED, "")
    }
}