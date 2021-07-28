package com.savannasolutions.SmartContractVerifierServer.messenger.services

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.requests.*
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.*
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessengerService constructor(val messagesRepository: MessagesRepository,
                                    val messageStatusRepository: MessageStatusRepository,
                                    val userRepository: UserRepository,
                                    val agreementsRepository: AgreementsRepository){

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
        if(!userRepository.existsById(sendMessageRequest.SendingUser))
            return SendMessageResponse(status = ResponseStatus.FAILED)

        if(!agreementsRepository.existsById(sendMessageRequest.AgreementID))
            return SendMessageResponse(status = ResponseStatus.FAILED)

        if(sendMessageRequest.Message.isEmpty())
            return SendMessageResponse(status = ResponseStatus.FAILED)

        val user = userRepository.getById(sendMessageRequest.SendingUser)
        val agreement = agreementsRepository.getById(sendMessageRequest.AgreementID)

        var message = Messages(UUID.fromString("eebe3abc-b594-4a2f-a7dc-246bad26aaa5"),
                                sendMessageRequest.Message,
                                Date())
        message.apply { agreements = agreement }
        message.apply { sender = user }

        message = messagesRepository.save(message)
        return SendMessageResponse(message.messageID, ResponseStatus.SUCCESSFUL)
    }

    fun setMessageAsRead(setMessageAsReadRequest: SetMessageAsReadRequest): SetMessageAsReadResponse{
        return SetMessageAsReadResponse(status = ResponseStatus.FAILED)
    }

}