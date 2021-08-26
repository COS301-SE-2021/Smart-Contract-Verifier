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
import java.util.*
import kotlin.collections.ArrayList

@Service
class ContactListService(   val contactListRepository: ContactListRepository,
                            val userRepository: UserRepository,
                            val contactListProfileRepository: ContactListProfileRepository, ){

    fun addUserToContactList(userID: String, contactListID: UUID, addUserToContactListRequest: AddUserToContactListRequest ): AddUserToContactListResponse{
        if(addUserToContactListRequest.UserAlias.isEmpty())
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        if(addUserToContactListRequest.UserID.isEmpty())
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        if(!userRepository.existsById(addUserToContactListRequest.UserID))
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        if(!contactListRepository.existsById(contactListID))
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        val tempContactList = contactListRepository.getById(contactListID)
        val tempUser = userRepository.getById(addUserToContactListRequest.UserID)

        if(contactListProfileRepository.getAllByContactListAndUser(tempContactList,tempUser) != null)
        {
            if(contactListProfileRepository.getAllByContactListAndUser(tempContactList,tempUser)!!.size > 1)
                return AddUserToContactListResponse(ResponseStatus.FAILED)
        }

        if(contactListProfileRepository.existsByContactAliasAndContactListAndUser(addUserToContactListRequest.UserAlias,tempContactList,tempUser))
            return AddUserToContactListResponse(ResponseStatus.FAILED)

        var contactListProfile = ContactListProfile(contactAlias = addUserToContactListRequest.UserAlias)
        contactListProfile = contactListProfile.apply { contactList = tempContactList }
        contactListProfile = contactListProfile.apply { user = tempUser }

        contactListProfileRepository.save(contactListProfile)

        return AddUserToContactListResponse(ResponseStatus.SUCCESSFUL)
    }

    fun createContactList(userID: String, contactListName: String): CreateContactListResponse{
        if(contactListName.isEmpty())
            return CreateContactListResponse(status = ResponseStatus.FAILED)

        if(!userRepository.existsById(userID))
            return CreateContactListResponse(status = ResponseStatus.FAILED)

        val user = userRepository.getById(userID)

        if(contactListRepository.existsByOwnerAndContactListName(user,contactListName))
            return CreateContactListResponse(status = ResponseStatus.FAILED)

        var contactList = ContactList(contactListName = contactListName)
        contactList = contactList.apply { owner = user }

        contactList = contactListRepository.save(contactList)

        return CreateContactListResponse(contactList.contactListID, ResponseStatus.SUCCESSFUL)
    }

    fun removeUserFromContactList(userID: String, contactListID: UUID, removeUserID: String): RemoveUserFromContactListResponse{
        if(removeUserID.isEmpty())
            return RemoveUserFromContactListResponse(ResponseStatus.FAILED)

        if(!userRepository.existsById(removeUserID))
            return RemoveUserFromContactListResponse(ResponseStatus.FAILED)

        if(!contactListRepository.existsById(contactListID))
            return RemoveUserFromContactListResponse(ResponseStatus.FAILED)

        val user = userRepository.getById(removeUserID)
        val contactList = contactListRepository.getById(contactListID)

        if(!contactListProfileRepository.existsByContactListAndUser(contactList, user))
            return RemoveUserFromContactListResponse(ResponseStatus.FAILED)

        val contactListProfile = contactListProfileRepository.getByContactListAndUser(contactList, user)!!

        contactListProfileRepository.delete(contactListProfile)

        if(contactListProfileRepository.existsById(contactListProfile.ProfileID!!))
            return RemoveUserFromContactListResponse(ResponseStatus.FAILED)

        return RemoveUserFromContactListResponse(ResponseStatus.SUCCESSFUL)
    }

    fun retrieveContactList(userID: String, contactListID: UUID):RetrieveContactListResponse
    {
        if(!contactListRepository.existsById(contactListID))
            return RetrieveContactListResponse(status = ResponseStatus.FAILED)

        val contactList = contactListRepository.getById(contactListID)

        if(contactList.owner.publicWalletID != userID)
            return RetrieveContactListResponse(status = ResponseStatus.FAILED)

        contactList.contactListProfiles?: return RetrieveContactListResponse(emptyList(), ResponseStatus.SUCCESSFUL)

        val list = ArrayList<ContactListAliasWalletResponse>()
        val contactListProfiles = contactListProfileRepository.getAllByContactList(contactList)

        for(cLP in contactListProfiles)
        {
            list.add(ContactListAliasWalletResponse( cLP.contactAlias, cLP.user.publicWalletID))
        }

        return RetrieveContactListResponse(list,ResponseStatus.SUCCESSFUL)
    }

    fun retrieveUserContactLists(userID: String): RetrieveUserContactListResponse{
        if(!userRepository.existsById(userID))
            return RetrieveUserContactListResponse(status = ResponseStatus.FAILED)

        val user = userRepository.getById(userID)
        val contactLists = contactListRepository.getAllByOwner(user)

        contactLists?:return RetrieveUserContactListResponse(emptyList(), ResponseStatus.SUCCESSFUL)

        val resultList = ArrayList<ContactListIDContactListNameResponse>()

        for(list in contactLists)
        {
            resultList.add(ContactListIDContactListNameResponse(list.contactListName, list.contactListID!!))
        }

        return RetrieveUserContactListResponse(resultList, ResponseStatus.SUCCESSFUL)
    }
}