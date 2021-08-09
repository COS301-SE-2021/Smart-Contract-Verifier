package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.common.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.ConditionResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.common.UserResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.GetNonceRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.RetrieveUserAgreementsRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.UserExistsRequest
import com.savannasolutions.SmartContractVerifierServer.user.responses.AddUserResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.GetNonceResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveUserAgreementsResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.UserExistsResponse
import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

@Service
class UserService(  val userRepository: UserRepository,
                    val agreementsRepository: AgreementsRepository,
                    val conditionsRepository: ConditionsRepository) {

    fun addUser(addUserRequest: AddUserRequest): AddUserResponse {
        val wID = addUserRequest.WalletID
        if(userRepository.existsById(wID))
            return AddUserResponse(status = ResponseStatus.FAILED)

        val user = User(publicWalletID = addUserRequest.WalletID, alias = addUserRequest.Alias)

        userRepository.save(user)

        return  AddUserResponse(status = ResponseStatus.SUCCESSFUL)
    }


    fun retrieveUserAgreements(retrieveUserAgreementsRequest: RetrieveUserAgreementsRequest): RetrieveUserAgreementsResponse {
        if(retrieveUserAgreementsRequest.UserID.isEmpty())
            return RetrieveUserAgreementsResponse(status = ResponseStatus.FAILED)

        if(!userRepository.existsById(retrieveUserAgreementsRequest.UserID))
            return RetrieveUserAgreementsResponse(status = ResponseStatus.FAILED)

        val user = userRepository.getById(retrieveUserAgreementsRequest.UserID)

        if(user.agreements.isEmpty())
            return RetrieveUserAgreementsResponse(emptyList(),ResponseStatus.SUCCESSFUL)

        val agreementList = agreementsRepository.getAllByUsersContaining(user)
        val list = ArrayList<AgreementResponse>()

        if(agreementList != null)
        {
            for(agreement in agreementList)
            {
                val conditions = conditionsRepository.getAllByContract(agreement)
                val conditionList = ArrayList<ConditionResponse>()
                if(conditions != null)
                {
                    for (cond in conditions)
                    {
                        conditionList.add(
                            ConditionResponse(cond.conditionID,
                                cond.conditionDescription,
                                UserResponse(cond.proposingUser.publicWalletID),
                                cond.proposalDate,
                                agreement.ContractID,
                                cond.conditionStatus
                            )
                        )
                    }
                }



                val tempArg = AgreementResponse(agreement.ContractID,
                    agreement.AgreementTitle,
                    agreement.AgreementDescription,
                    agreement.DurationConditionUUID,
                    agreement.PaymentConditionUUID,
                    UserResponse(agreement.users.elementAt(0).publicWalletID),
                    UserResponse(agreement.users.elementAt(1).publicWalletID),
                    agreement.CreatedDate,
                    agreement.SealedDate,
                    agreement.MovedToBlockChain,
                    conditionList,
                    agreement.AgreementImageURL,)

                list.add(tempArg)
            }
        }



        return RetrieveUserAgreementsResponse(list,ResponseStatus.SUCCESSFUL)
    }

    fun userExists(userExistsRequest: UserExistsRequest): UserExistsResponse
    {
        if(userExistsRequest.walletID.isEmpty())
            return UserExistsResponse(status = ResponseStatus.FAILED)

        return if(userRepository.existsById(userExistsRequest.walletID))
            UserExistsResponse(true, ResponseStatus.SUCCESSFUL)
        else
            UserExistsResponse(status = ResponseStatus.SUCCESSFUL)
    }


    fun getNonce(getNonceRequest: GetNonceRequest): GetNonceResponse{
        if (userRepository.existsById(getNonceRequest.UserID)) {
            val user = userRepository.getById(getNonceRequest.UserID)
            val nonce = ThreadLocalRandom.current().nextLong(1000000000, 9999999999)
            user.nonce = nonce
            userRepository.save(user)
            return GetNonceResponse(nonce, ResponseStatus.SUCCESSFUL)
        }
        return GetNonceResponse(0, ResponseStatus.FAILED)
    }
}