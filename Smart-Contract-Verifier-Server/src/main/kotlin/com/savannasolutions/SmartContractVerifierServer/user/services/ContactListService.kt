package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.*
import com.savannasolutions.SmartContractVerifierServer.user.responses.*
import org.springframework.stereotype.Service

@Service
class ContactListService(   val contactListRepository: ContactListRepository,
                            val userRepository: UserRepository,
                            val contactListProfileRepository: ContactListProfileRepository,){

    fun addUserToContactList(addUserToContactListRequest: AddUserToContactListRequest ): AddUserToContactListResponse{
        if(addUserToContactListRequest.UserAlias.isEmpty())
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        if(addUserToContactListRequest.UserID.isEmpty())
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        if(!userRepository.existsById(addUserToContactListRequest.UserID))
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        if(!contactListRepository.existsById(addUserToContactListRequest.ContactListID))
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        val vcontactList = contactListRepository.getById(addUserToContactListRequest.ContactListID)
        val vuser = userRepository.getById(addUserToContactListRequest.UserID)

        if(contactListProfileRepository.getAllByContactListAndUser(vcontactList,vuser) != null)
        {
            if(contactListProfileRepository.getAllByContactListAndUser(vcontactList,vuser)!!.size > 1)
                return AddUserToContactListResponse(ResponseStatus.FAILED)
        }

        if(contactListProfileRepository.existByAliasAnAndContactListAndUser(vcontactList,vuser,addUserToContactListRequest.UserAlias))
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        var contactListProfile = ContactListProfile(ContactAlias = addUserToContactListRequest.UserAlias)
        contactListProfile = contactListProfile.apply { contactList = vcontactList }
        contactListProfile = contactListProfile.apply { user = vuser }

        contactListProfileRepository.save(contactListProfile)

        return AddUserToContactListResponse(ResponseStatus.SUCCESSFUL)
    }

    fun createContactList(createContactListRequest: CreateContactListRequest): CreateContactListResponse{
        if(createContactListRequest.ContactListName.isEmpty())
            return CreateContactListResponse(status = ResponseStatus.FAILED)

        if(createContactListRequest.UserID.isEmpty())
            return CreateContactListResponse(status = ResponseStatus.FAILED)

        if(!userRepository.existsById(createContactListRequest.UserID))
            return CreateContactListResponse(status = ResponseStatus.FAILED)

        val user = userRepository.getById(createContactListRequest.UserID)

        if(contactListRepository.existsByOwnerAndContactListName(user,createContactListRequest.ContactListName))
            return CreateContactListResponse(status = ResponseStatus.FAILED)

        var contactList = ContactList(contactListName = createContactListRequest.ContactListName)
        contactList = contactList.apply { owner = user }

        contactList = contactListRepository.save(contactList)

        return CreateContactListResponse(contactList.contactListID, ResponseStatus.FAILED)
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