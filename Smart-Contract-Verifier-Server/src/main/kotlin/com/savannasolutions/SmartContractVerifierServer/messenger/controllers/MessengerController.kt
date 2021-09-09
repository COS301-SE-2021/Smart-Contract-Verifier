package com.savannasolutions.SmartContractVerifierServer.messenger.controllers

import com.savannasolutions.SmartContractVerifierServer.messenger.requests.*
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.GetAllMessagesByAgreementResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.services.MessengerService
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController
class MessengerController constructor(private val messengerService: MessengerService) {

    @GetMapping("/user/{userId}/agreement/{agreementId}/message")
    fun getAllMessagesByAgreement(@PathVariable userId: String, @PathVariable agreementId: UUID,) =
        messengerService.getAllMessagesByAgreement(userId, agreementId)

    @GetMapping("/user/{userId}/message")
    fun getAllMessagesByUser(@PathVariable userId: String) =
        messengerService.getAllMessagesByUser(userId)

    @GetMapping("/user/{userId}/message/{messageId}")
    fun getMessageDetail(@PathVariable userId: String, @PathVariable messageId: UUID,) =
        messengerService.getMessageDetail(userId, messageId)

    @PostMapping("/user/{userId}/agreement/{agreementId}/message")
    fun sendMessage(@PathVariable userId: String,
                    @PathVariable agreementId: UUID,
                    @RequestBody sendMessageRequest: SendMessageRequest,) =
        messengerService.sendMessage(userId, agreementId, sendMessageRequest,)

    @PutMapping("/user/{userId}/message/{messageId}")
    fun setMessageAsRead(@PathVariable userId: String, @PathVariable messageId: UUID,) =
        messengerService.setMessageAsRead(userId, messageId)

    @GetMapping("/user/{userId}/message/unread")
    fun getUnreadMessages(@PathVariable userId: String,) =
        messengerService.getUnreadMessages(userId)
}