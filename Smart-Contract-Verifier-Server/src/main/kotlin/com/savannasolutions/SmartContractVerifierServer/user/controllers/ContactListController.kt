package com.savannasolutions.SmartContractVerifierServer.user.controllers

import com.savannasolutions.SmartContractVerifierServer.user.requests.*
import com.savannasolutions.SmartContractVerifierServer.user.responses.*
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController
class ContactListController constructor(private val contactListService: ContactListService) {

    @PutMapping("/user/{userId}/contactList/{contactListId}")
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

    @GetMapping("/user/{userId}/contactList/{contactListId}")
    fun retrieveContactList(@PathVariable userId: String, @PathVariable contactListId: UUID): RetrieveContactListResponse{
        return contactListService.retrieveContactList(userId, contactListId)
    }

    @GetMapping("/user/{userId}/contactList")
    fun retrieveUserContactList(@PathVariable userId: String,): RetrieveUserContactListResponse{
        return contactListService.retrieveUserContactLists(userId)
    }

}