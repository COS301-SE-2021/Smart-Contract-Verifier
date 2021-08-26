package com.savannasolutions.SmartContractVerifierServer.user.controllers

import com.savannasolutions.SmartContractVerifierServer.user.requests.*
import com.savannasolutions.SmartContractVerifierServer.user.responses.*
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController
class ContactListController constructor(private val contactListService: ContactListService) {

    @PostMapping("/user/{userId}/contactList/{contactListId}")
    fun addUserToContactList(@PathVariable userId: String,
                             @PathVariable contactListId: UUID,
                             @RequestBody addUserToContactListRequest: AddUserToContactListRequest): AddUserToContactListResponse{
        return contactListService.addUserToContactList(userId, contactListId, addUserToContactListRequest)
    }

    @PostMapping("/user/{userId}/contactList/{contactListName}")
    fun createContactList(@PathVariable userId: String,
                          @PathVariable contactListName: String): CreateContactListResponse{
        return contactListService.createContactList(userId, contactListName,)
    }

    @DeleteMapping("/user/{userId}/contactList/{contactListId}/{removedUserId}")
    fun removeUserFromContactList(@PathVariable userId: String,
                                  @PathVariable contactListId: UUID,
                                  @PathVariable removedUserId: String,): RemoveUserFromContactListResponse{
        return contactListService.removeUserFromContactList(userId, contactListId, removedUserId)
    }

    @PostMapping("/user/{userId}/contactList/{contactListId}")
    fun retrieveContactList(@PathVariable userId: String, contactListId: UUID): RetrieveContactListResponse{
        return contactListService.retrieveContactList(userId, contactListId)
    }

    @PostMapping("/user/{userId}/contactList/{contactListId}")
    fun retrieveUserContactList(@PathVariable userId: String,
                                @PathVariable contactListId: UUID): RetrieveUserContactListResponse{
        return contactListService.retrieveUserContactLists(userId, contactListId)
    }

}