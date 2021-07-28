package com.savannasolutions.SmartContractVerifierServer.messenger.services

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.requests.*
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.*
import org.springframework.stereotype.Service

@Service
class MessengerService constructor(val messagesRepository: MessagesRepository,
                                    val messageStatusRepository: MessageStatusRepository,){

    fun getAllMessagesByAgreement(getAllMessagesByAgreementRequest: GetAllMessagesByAgreementRequest): GetAllMessagesByAgreementResponse{
        return GetAllMessagesByAgreementResponse(status = ResponseStatus.FAILED)
    }

    fun getAllMessagesByUser(getAllMessagesByUserRequest: GetAllMessagesByUserRequest): GetAllMessagesByUserResponse{
        return GetAllMessagesByUserResponse(status = ResponseStatus.FAILED)
    }

    fun getMessageDetail(getMessageDetailRequest: GetMessageDetailRequest): GetMessageDetailResponse{
        return GetMessageDetailResponse(status = ResponseStatus.FAILED)
    }

    fun sendMessage(sendMessageRequest: SendMessageRequest): SendMessageResponse{
        return SendMessageResponse(status = ResponseStatus.FAILED)
    }

    fun setMessageAsRead(setMessageAsReadRequest: SetMessageAsReadRequest): SetMessageAsReadResponse{
        return SetMessageAsReadResponse(status = ResponseStatus.FAILED)
    }

}