package com.savannasolutions.SmartContractVerifierServer.user.controllers

import com.savannasolutions.SmartContractVerifierServer.user.requests.*
import com.savannasolutions.SmartContractVerifierServer.user.responses.*
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/contactlist")
class ContactListController constructor(private val contactListService: ContactListService) {

    @PostMapping("/add-user-to-contact-list")
    fun addUserToContactList(addUserToContactListRequest: AddUserToContactListRequest): AddUserToContactListResponse{
        return contactListService.addUserToContactList(addUserToContactListRequest)
    }

    @PostMapping("/create-contact-list")
    fun createContactList(createContactListRequest: CreateContactListRequest): CreateContactListResponse{
        return contactListService.createContactList(createContactListRequest)
    }

    @PostMapping("/remove-user-from-contact-list")
    fun removeUserFromContactList(removeUserFromContactListRequest: RemoveUserFromContactListRequest): RemoveUserFromContactListResponse{
        return contactListService.removeUserFromContactList(removeUserFromContactListRequest)
    }

    @PostMapping("/retrieve-contact-list")
    fun retrieveContactList(retrieveContactListRequest: RetrieveContactListRequest): RetrieveContactListResponse{
        return contactListService.retrieveContactList(retrieveContactListRequest)
    }

    @PostMapping("/retrieve-user-contact-list")
    fun retrieveUserContactList(retrieveUserContactListRequest: RetrieveUserContactListRequest): RetrieveUserContactListResponse{
        return contactListService.retrieveUserContactLists(retrieveUserContactListRequest)
    }

}