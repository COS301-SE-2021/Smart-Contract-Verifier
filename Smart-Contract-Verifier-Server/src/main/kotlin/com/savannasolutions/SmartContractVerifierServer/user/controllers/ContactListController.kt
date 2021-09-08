package com.savannasolutions.SmartContractVerifierServer.user.controllers

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.user.requests.AddUserToContactListRequest
import com.savannasolutions.SmartContractVerifierServer.user.responses.CreateContactListResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveContactListResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveUserContactListResponse
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController
class ContactListController constructor(private val contactListService: ContactListService) {

    @PutMapping("/user/{userId}/contactList/{contactListId}")
    fun addUserToContactList(@PathVariable userId: String,
                             @PathVariable contactListId: UUID,
                             @RequestBody addUserToContactListRequest: AddUserToContactListRequest): ApiResponse<Objects>{
        return contactListService.addUserToContactList(userId, contactListId, addUserToContactListRequest)
    }

    @PostMapping("/user/{userId}/contactList/{contactListName}")
    fun createContactList(@PathVariable userId: String,
                          @PathVariable contactListName: String): ApiResponse<CreateContactListResponse>{
        return contactListService.createContactList(userId, contactListName,)
    }

    @DeleteMapping("/user/{userId}/contactList/{contactListId}/{removedUserId}")
    fun removeUserFromContactList(@PathVariable userId: String,
                                  @PathVariable contactListId: UUID,
                                  @PathVariable removedUserId: String,): ApiResponse<Objects>{
        return contactListService.removeUserFromContactList(userId, contactListId, removedUserId)
    }

    @GetMapping("/user/{userId}/contactList/{contactListId}")
    fun retrieveContactList(@PathVariable userId: String, @PathVariable contactListId: UUID): ApiResponse<RetrieveContactListResponse>{
        return contactListService.retrieveContactList(userId, contactListId)
    }

    @GetMapping("/user/{userId}/contactList")
    fun retrieveUserContactList(@PathVariable userId: String,): ApiResponse<RetrieveUserContactListResponse>{
        return contactListService.retrieveUserContactLists(userId)
    }

}