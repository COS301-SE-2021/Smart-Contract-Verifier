package com.savannasolutions.SmartContractVerifierServer.messenger.services

import com.savannasolutions.SmartContractVerifierServer.common.MessageResponse
import com.savannasolutions.SmartContractVerifierServer.common.MessageStatusResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.common.UserResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.requests.*
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.*
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class MessengerService constructor(val messagesRepository: MessagesRepository,
                                    val messageStatusRepository: MessageStatusRepository,
                                    val userRepository: UserRepository,
                                    val agreementsRepository: AgreementsRepository){

    fun getAllMessagesByAgreement(getAllMessagesByAgreementRequest: GetAllMessagesByAgreementRequest): GetAllMessagesByAgreementResponse{
        if(!userRepository.existsById(getAllMessagesByAgreementRequest.RequestingUser))
            return GetAllMessagesByAgreementResponse(status = ResponseStatus.FAILED)

        if(!agreementsRepository.existsById(getAllMessagesByAgreementRequest.AgreementID))
            return GetAllMessagesByAgreementResponse(status = ResponseStatus.FAILED)

        val agreement = agreementsRepository.getById(getAllMessagesByAgreementRequest.AgreementID)

        val messageList = messagesRepository.getAllByAgreements(agreement) ?:
            return GetAllMessagesByAgreementResponse(emptyList(), status = ResponseStatus.SUCCESSFUL)

        val messageResponseList = ArrayList<MessageResponse>()

        for(message in messageList){
            val messageStatuses = messageStatusRepository.getAllByMessage(message)
            val messageStatusList = ArrayList<MessageStatusResponse>()
            if(messageStatuses != null)
            {
                for(messageStatus in messageStatuses)
                {
                    var read = true
                    if(messageStatus.ReadDate == null)
                        read = false
                    val tempMessageStatusResponse = MessageStatusResponse(messageStatus.recipient.publicWalletID,
                                                                            read,
                                                                            messageStatus.ReadDate)
                    messageStatusList.add(tempMessageStatusResponse)
                }
            }
            val messageResponse = MessageResponse(message.messageID,
                                                    UserResponse(message.sender.publicWalletID),
                                                    message.sendDate,
                                                    message.agreements.ContractID,
                                                    message.message,
                                                    messageStatusList,)
            messageResponseList.add(messageResponse)
        }

        return GetAllMessagesByAgreementResponse(messageResponseList, ResponseStatus.SUCCESSFUL)
    }

    fun getAllMessagesByUser(getAllMessagesByUserRequest: GetAllMessagesByUserRequest): GetAllMessagesByUserResponse{
        return GetAllMessagesByUserResponse(status = ResponseStatus.FAILED)
    }

    fun getMessageDetail(getMessageDetailRequest: GetMessageDetailRequest): GetMessageDetailResponse{
        return GetMessageDetailResponse(status = ResponseStatus.FAILED)
    }

    fun sendMessage(sendMessageRequest: SendMessageRequest): SendMessageResponse{
        //TODO Implement setting jury as recipients as well
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
        message = message.apply { agreements = agreement }
        message = message.apply { sender = user }

        val usersInAgreement = userRepository.getUsersByAgreementsContaining(agreement)
        val messageStatusList = ArrayList<MessageStatus>()

        message = messagesRepository.save(message)

        for(otherUser in usersInAgreement)
        {
            if(otherUser != user)
            {
                var tempMessageStatus = MessageStatus(UUID.fromString("eebe3abc-b594-4a2f-a7dc-246bad26aaa5"))
                tempMessageStatus.recipient = otherUser
                tempMessageStatus.message = message;
                tempMessageStatus = messageStatusRepository.save(tempMessageStatus)
                messageStatusList.add(tempMessageStatus)
            }
        }

        message.messageStatuses = messageStatusList

        message = messagesRepository.save(message)

        return SendMessageResponse(message.messageID, ResponseStatus.SUCCESSFUL)
    }

    fun setMessageAsRead(setMessageAsReadRequest: SetMessageAsReadRequest): SetMessageAsReadResponse{
        return SetMessageAsReadResponse(status = ResponseStatus.FAILED)
    }

}