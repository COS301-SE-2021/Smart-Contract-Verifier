package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.RetrieveUserAgreementsRequest
import com.savannasolutions.SmartContractVerifierServer.user.responses.AddUserResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveUserAgreementsResponse
import java.util.*
import kotlin.collections.ArrayList

class UserService(  val userRepository: UserRepository,
                    val agreementsRepository: AgreementsRepository) {

    fun addUser(addUserRequest: AddUserRequest): AddUserResponse {
        if(userRepository.existsById(addUserRequest.WalletID))
            return AddUserResponse(status = ResponseStatus.FAILED)

        val user = User(publicWalletID = addUserRequest.WalletID, alias = "")

        userRepository.save(user)

        return  AddUserResponse(status = ResponseStatus.SUCCESSFUL)
    }


    fun retrieveUserAgreements(retrieveUserAgreementsRequest: RetrieveUserAgreementsRequest): RetrieveUserAgreementsResponse {
        if(retrieveUserAgreementsRequest.UserID.isEmpty())
            return RetrieveUserAgreementsResponse(status = ResponseStatus.FAILED)

        if(!userRepository.existsById(retrieveUserAgreementsRequest.UserID))
            return RetrieveUserAgreementsResponse(status = ResponseStatus.FAILED)

        val user = userRepository.getById(retrieveUserAgreementsRequest.UserID)

        user.agreements?:return RetrieveUserAgreementsResponse(emptyList(),ResponseStatus.SUCCESSFUL)

        val list = ArrayList<UUID>()

        for(agreement in user.agreements!!)
            list.add(agreement.ContractID)

        return RetrieveUserAgreementsResponse(list,ResponseStatus.SUCCESSFUL)
    }
}