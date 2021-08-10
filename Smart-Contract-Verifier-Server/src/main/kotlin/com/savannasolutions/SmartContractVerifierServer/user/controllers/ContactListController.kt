package com.savannasolutions.SmartContractVerifierServer.user.controllers

import com.savannasolutions.SmartContractVerifierServer.user.requests.*
import com.savannasolutions.SmartContractVerifierServer.user.responses.*
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/contactlist")
class ContactListController constructor(private val contactListService: ContactListService) {

    @PostMapping("/add-user-to-contact-list")
    fun addUserToContactList(@RequestBody addUserToContactListRequest: AddUserToContactListRequest): AddUserToContactListResponse{
        return contactListService.addUserToContactList(addUserToContactListRequest)
    }

    @PostMapping("/create-contact-list")
    fun createContactList(@RequestBody createContactListRequest: CreateContactListRequest): CreateContactListResponse{
        return contactListService.createContactList(createContactListRequest)
    }

    @PostMapping("/remove-user-from-contact-list")
    fun removeUserFromContactList(@RequestBody removeUserFromContactListRequest: RemoveUserFromContactListRequest): RemoveUserFromContactListResponse{
        return contactListService.removeUserFromContactList(removeUserFromContactListRequest)
    }

    @PostMapping("/retrieve-contact-list")
    fun retrieveContactList(@RequestBody retrieveContactListRequest: RetrieveContactListRequest): RetrieveContactListResponse{
        return contactListService.retrieveContactList(retrieveContactListRequest)
    }

    @PostMapping("/retrieve-user-contact-list")
    fun retrieveUserContactList(@RequestBody retrieveUserContactListRequest: RetrieveUserContactListRequest): RetrieveUserContactListResponse{
        return contactListService.retrieveUserContactLists(retrieveUserContactListRequest)
    }

}