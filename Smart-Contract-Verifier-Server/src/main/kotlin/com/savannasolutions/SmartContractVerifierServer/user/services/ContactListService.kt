package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.*
import com.savannasolutions.SmartContractVerifierServer.user.responses.*
import org.springframework.stereotype.Service

@Service
class ContactListService(   val contactListRepository: ContactListRepository,
                            val userRepository: UserRepository){

    fun addUserToContactList(addUserToContactListRequest: AddUserToContactListRequest ): AddUserToContactListResponse{
        return AddUserToContactListResponse(ResponseStatus.FAILED)
    }

    fun createContactList(createContactListRequest: CreateContactListRequest): CreateContactListResponse{
        return CreateContactListResponse(status = ResponseStatus.FAILED)
    }

    fun removeUserFromContactList(removeUserFromContactListRequest: RemoveUserFromContactListRequest): RemoveUserFromContactListResponse{
        return RemoveUserFromContactListResponse(ResponseStatus.FAILED)
    }

    fun retrieveContactList(retrieveContactListRequest: RetrieveContactListRequest):RetrieveContactListResponse{
        return RetrieveContactListResponse(status = ResponseStatus.FAILED)
    }

    fun retrieveUserAgreements(retrieveUserAgreementsRequest: RetrieveUserAgreementsRequest): RetrieveUserAgreementsResponse{
        return RetrieveUserAgreementsResponse(status = ResponseStatus.FAILED)
    }

    fun retrieveUserContactLists(retrieveUserContactListRequest: RetrieveUserContactListRequest): RetrieveUserContactListResponse{
        return RetrieveUserContactListResponse(status = ResponseStatus.FAILED)
    }
}