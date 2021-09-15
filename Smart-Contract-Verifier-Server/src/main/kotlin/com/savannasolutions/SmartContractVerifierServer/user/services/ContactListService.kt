package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ContactListAliasWalletResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ContactListIDContactListNameResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.common.responseErrorMessages.commonResponseErrorMessages
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.AddUserToContactListRequest
import com.savannasolutions.SmartContractVerifierServer.user.responses.CreateContactListResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveContactListResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveUserContactListResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class ContactListService(   val contactListRepository: ContactListRepository,
                            val userRepository: UserRepository,
                            val contactListProfileRepository: ContactListProfileRepository, ){

    fun addUserToContactList(userID: String, contactListID: UUID, addUserToContactListRequest: AddUserToContactListRequest ): ApiResponse<Objects>{
        if(addUserToContactListRequest.UserAlias.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "User alias is empty")

        if(addUserToContactListRequest.UserID.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "User id is empty")

        if(!userRepository.existsById(addUserToContactListRequest.UserID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userDoesNotExist)

        if(!contactListRepository.existsById(contactListID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.contactListDoesNotExist)

        if(userID == addUserToContactListRequest.UserID)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "User cannot add themselves to a contact list")

        val tempContactList = contactListRepository.getById(contactListID)
        val tempUser = userRepository.getById(addUserToContactListRequest.UserID)

        if(contactListProfileRepository.getAllByContactListAndUser(tempContactList,tempUser) != null)
        {
            if(contactListProfileRepository.getAllByContactListAndUser(tempContactList,tempUser)!!.size > 1)
                return ApiResponse(status = ResponseStatus.FAILED,
                message = "User already added to contact list")
        }

        if(contactListProfileRepository.existsByContactAliasAndContactListAndUser(addUserToContactListRequest.UserAlias,tempContactList,tempUser))
            return ApiResponse(ResponseStatus.FAILED,
            message = "User already exists with alias in contact list")

        var contactListProfile = ContactListProfile(contactAlias = addUserToContactListRequest.UserAlias)
        contactListProfile = contactListProfile.apply { contactList = tempContactList }
        contactListProfile = contactListProfile.apply { user = tempUser }

        contactListProfileRepository.save(contactListProfile)

        return ApiResponse(status = ResponseStatus.SUCCESSFUL)
    }

    fun createContactList(userID: String, contactListName: String): ApiResponse<CreateContactListResponse>{
        if(contactListName.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "Contact list name is empty")

        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userDoesNotExist)

        val user = userRepository.getById(userID)

        if(contactListRepository.existsByOwnerAndContactListName(user,contactListName))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "User already has a contact list with passed name")

        var contactList = ContactList(contactListName = contactListName)
        contactList = contactList.apply { owner = user }

        contactList = contactListRepository.save(contactList)

        return ApiResponse(responseObject = CreateContactListResponse(contactList.contactListID),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun removeUserFromContactList(userID: String, contactListID: UUID, removeUserID: String): ApiResponse<Objects>{
        if(removeUserID.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "UserID is empty")

        if(!userRepository.existsById(removeUserID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userDoesNotExist)

        if(!contactListRepository.existsById(contactListID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.contactListDoesNotExist)

        val user = userRepository.getById(removeUserID)
        val contactList = contactListRepository.getById(contactListID)

        if(!contactListProfileRepository.existsByContactListAndUser(contactList, user))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "User is not part of contact list")

        val contactListProfile = contactListProfileRepository.getByContactListAndUser(contactList, user)!!

        contactListProfileRepository.delete(contactListProfile)

        if(contactListProfileRepository.existsById(contactListProfile.ProfileID!!))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "Failed to delete user")

        return ApiResponse(status = ResponseStatus.SUCCESSFUL)
    }

    fun retrieveContactList(userID: String, contactListID: UUID): ApiResponse<RetrieveContactListResponse>
    {
        if(!contactListRepository.existsById(contactListID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.contactListDoesNotExist)

        val contactList = contactListRepository.getById(contactListID)

        if(contactList.owner.publicWalletID != userID)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "User is not the owner of contact list")

        contactList.contactListProfiles?: return ApiResponse(responseObject = RetrieveContactListResponse(emptyList()),
            status = ResponseStatus.SUCCESSFUL)

        val list = ArrayList<ContactListAliasWalletResponse>()
        val contactListProfiles = contactListProfileRepository.getAllByContactList(contactList)

        for(cLP in contactListProfiles)
        {
            list.add(ContactListAliasWalletResponse( cLP.contactAlias, cLP.user.publicWalletID))
        }

        return ApiResponse(responseObject = RetrieveContactListResponse(list),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun retrieveUserContactLists(userID: String): ApiResponse<RetrieveUserContactListResponse>{
        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userDoesNotExist)

        val user = userRepository.getById(userID)
        val contactLists = contactListRepository.getAllByOwner(user)

        contactLists?:return ApiResponse(responseObject = RetrieveUserContactListResponse(emptyList()),
            status = ResponseStatus.SUCCESSFUL)

        val resultList = ArrayList<ContactListIDContactListNameResponse>()

        for(list in contactLists)
        {
            resultList.add(ContactListIDContactListNameResponse(list.contactListName, list.contactListID!!))
        }

        return ApiResponse(responseObject = RetrieveUserContactListResponse(resultList),
            status = ResponseStatus.SUCCESSFUL)
    }
}