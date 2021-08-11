package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.common.ContactListIDContactListNameResponse
import com.savannasolutions.SmartContractVerifierServer.common.ContactListAliasWalletResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.*
import com.savannasolutions.SmartContractVerifierServer.user.responses.*
import org.springframework.stereotype.Service
import kotlin.collections.ArrayList

@Service
class ContactListService(   val contactListRepository: ContactListRepository,
                            val userRepository: UserRepository,
                            val contactListProfileRepository: ContactListProfileRepository, ){

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

        if(contactListProfileRepository.existsByContactAliasAndContactListAndUser(addUserToContactListRequest.UserAlias,vcontactList,vuser))
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        var contactListProfile = ContactListProfile(contactAlias = addUserToContactListRequest.UserAlias)
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

        return CreateContactListResponse(contactList.contactListID, ResponseStatus.SUCCESSFUL)
    }

    fun removeUserFromContactList(removeUserFromContactListRequest: RemoveUserFromContactListRequest): RemoveUserFromContactListResponse{
        if(removeUserFromContactListRequest.RemoveUserID.isEmpty())
            return RemoveUserFromContactListResponse(ResponseStatus.FAILED)

        if(!userRepository.existsById(removeUserFromContactListRequest.RemoveUserID))
            return RemoveUserFromContactListResponse(ResponseStatus.FAILED)

        if(!contactListRepository.existsById(removeUserFromContactListRequest.ContactListID))
            return RemoveUserFromContactListResponse(ResponseStatus.FAILED)

        val user = userRepository.getById(removeUserFromContactListRequest.RemoveUserID)
        val contactList = contactListRepository.getById(removeUserFromContactListRequest.ContactListID)

        if(!contactListProfileRepository.existsByContactListAndUser(contactList, user))
            return RemoveUserFromContactListResponse(ResponseStatus.FAILED)

        val contactListProfile = contactListProfileRepository.getByContactListAndUser(contactList, user)!!

        contactListProfileRepository.delete(contactListProfile)

        if(contactListProfileRepository.existsById(contactListProfile.ProfileID!!))
            return RemoveUserFromContactListResponse(ResponseStatus.FAILED)

        return RemoveUserFromContactListResponse(ResponseStatus.SUCCESSFUL)
    }

    fun retrieveContactList(retrieveContactListRequest: RetrieveContactListRequest):RetrieveContactListResponse
    {
        if(!contactListRepository.existsById(retrieveContactListRequest.ContactListID))
            return RetrieveContactListResponse(status = ResponseStatus.FAILED)

        val contactList = contactListRepository.getById(retrieveContactListRequest.ContactListID)
        contactList.contactListProfiles?: return RetrieveContactListResponse(emptyList(), ResponseStatus.SUCCESSFUL)

        val list = ArrayList<ContactListAliasWalletResponse>()
        val contactListProfiles = contactListProfileRepository.getAllByContactList(contactList)

        for(cLP in contactListProfiles)
        {
            list.add(ContactListAliasWalletResponse( cLP.contactAlias, cLP.user.publicWalletID))
        }

        return RetrieveContactListResponse(list,ResponseStatus.SUCCESSFUL)
    }

    fun retrieveUserContactLists(retrieveUserContactListRequest: RetrieveUserContactListRequest): RetrieveUserContactListResponse{
        if(retrieveUserContactListRequest.UserID.isEmpty())
            return RetrieveUserContactListResponse(status = ResponseStatus.FAILED)

        if(!userRepository.existsById(retrieveUserContactListRequest.UserID))
            return RetrieveUserContactListResponse(status = ResponseStatus.FAILED)

        val contactList = userRepository.getById(retrieveUserContactListRequest.UserID).contactList
        val user = userRepository.getById(retrieveUserContactListRequest.UserID)

        contactList?:return RetrieveUserContactListResponse(emptyList(), ResponseStatus.SUCCESSFUL)

        val resultList = ArrayList<ContactListIDContactListNameResponse>()
        val contactLists = contactListRepository.getAllByOwner(user)

        for(list in contactLists)
        {
            resultList.add(ContactListIDContactListNameResponse(list.contactListName, list.contactListID!!,))
        }

        return RetrieveUserContactListResponse(resultList, ResponseStatus.SUCCESSFUL)
    }
}