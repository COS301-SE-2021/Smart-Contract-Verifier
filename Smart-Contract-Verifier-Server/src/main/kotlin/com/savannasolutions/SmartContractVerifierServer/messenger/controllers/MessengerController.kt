package com.savannasolutions.SmartContractVerifierServer.messenger.controllers

import com.savannasolutions.SmartContractVerifierServer.messenger.requests.*
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.GetAllMessagesByAgreementResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.services.MessengerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/messenger")
class MessengerController constructor(private val messengerService: MessengerService) {

    @PostMapping("/get-all-messages-by-agreement")
    fun getAllMessagesByAgreement(@RequestBody getAllMessagesByAgreementRequest: GetAllMessagesByAgreementRequest) =
        messengerService.getAllMessagesByAgreement(getAllMessagesByAgreementRequest)

    @PostMapping("/get-all-messages-by-user")
    fun getAllMessagesByUser(@RequestBody getAllMessagesByUserRequest: GetAllMessagesByUserRequest) =
        messengerService.getAllMessagesByUser(getAllMessagesByUserRequest)

    @PostMapping("/get-message-detail")
    fun getMessageDetail(@RequestBody getMessageDetailRequest: GetMessageDetailRequest) =
        messengerService.getMessageDetail(getMessageDetailRequest)

    @PostMapping("/send-message")
    fun sendMessage(@RequestBody sendMessageRequest: SendMessageRequest) =
        messengerService.sendMessage(sendMessageRequest)

    @PostMapping("set-message-as-read")
    fun setMessageAsRead(@RequestBody setMessageAsReadRequest: SetMessageAsReadRequest) =
        messengerService.setMessageAsRead(setMessageAsReadRequest)
}