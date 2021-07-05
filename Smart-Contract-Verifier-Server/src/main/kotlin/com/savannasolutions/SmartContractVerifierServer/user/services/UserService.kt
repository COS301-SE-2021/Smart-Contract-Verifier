package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.RetrieveUserInfoRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.UpdateUserEmailRequest
import com.savannasolutions.SmartContractVerifierServer.user.responses.AddUserResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveUserInfoResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.UpdateUserEmailResponse

class UserService(  val userRepository: UserRepository,
                    val agreementsRepository: AgreementsRepository) {

    fun retrieveUserInfo(retrieveUserInfoRequest: RetrieveUserInfoRequest): RetrieveUserInfoResponse {
        return RetrieveUserInfoResponse(retrieveUserInfoRequest.UserID, status = ResponseStatus.FAILED)
    }

    fun updateUserEmail(updateUserEmailRequest: UpdateUserEmailRequest): UpdateUserEmailResponse {
        return UpdateUserEmailResponse(status = ResponseStatus.FAILED)
    }

    fun addUser(addUserRequest: AddUserRequest): AddUserResponse {
        return AddUserResponse(ResponseStatus.FAILED)
    }
}