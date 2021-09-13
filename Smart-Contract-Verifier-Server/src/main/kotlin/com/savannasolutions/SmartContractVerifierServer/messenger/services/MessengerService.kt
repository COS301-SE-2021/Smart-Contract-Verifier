package com.savannasolutions.SmartContractVerifierServer.messenger.services

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.*
import com.savannasolutions.SmartContractVerifierServer.common.responseErrorMessages.commonResponseErrorMessages
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.requests.SendMessageRequest
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.*
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessengerService constructor(val messagesRepository: MessagesRepository,
                                    val messageStatusRepository: MessageStatusRepository,
                                    val userRepository: UserRepository,
                                    val agreementsRepository: AgreementsRepository,
                                    val judgesRepository: JudgesRepository){

    private fun generateMessageResponseList(messageList : List<Messages>): List<MessageResponse>{
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
        return messageResponseList
    }

    fun getAllMessagesByAgreement(userID: String, agreementID: UUID): ApiResponse<GetAllMessagesByAgreementResponse>{
        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.userDoesNotExist)

        if(!agreementsRepository.existsById(agreementID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.agreementDoesNotExist)

        val agreement = agreementsRepository.getById(agreementID)

        val messageList = messagesRepository.getAllByAgreements(agreement) ?:
            return ApiResponse(responseObject = GetAllMessagesByAgreementResponse(emptyList()),
                status = ResponseStatus.SUCCESSFUL)

        val messageResponseList = generateMessageResponseList(messageList)

        return ApiResponse(responseObject = GetAllMessagesByAgreementResponse(messageResponseList),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun getAllMessagesByUser(userID: String): ApiResponse<GetAllMessagesByUserResponse>{
        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.userDoesNotExist)

        val user = userRepository.getById(userID)
        val sentMessageList = messagesRepository.getAllBySender(user)
        val receivedMessageList = messageStatusRepository.getAllByRecipient(user)
        val messageList = ArrayList<Messages>()
        if(sentMessageList != null)
            messageList.addAll(sentMessageList)

        if(receivedMessageList != null)
        {
            for(msg in receivedMessageList)
            {
                val message = messagesRepository.getById(msg.message.messageID)
                messageList.add(message)
            }
        }

        val messageResponseList = generateMessageResponseList(messageList)

        return ApiResponse(responseObject = GetAllMessagesByUserResponse(messageResponseList),
            status =  ResponseStatus.SUCCESSFUL)
    }

    fun getMessageDetail(userID: String, messageID: UUID): ApiResponse<GetMessageDetailResponse>{
        if(!messagesRepository.existsById(messageID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.messageDoesNotExist)

        val message = messagesRepository.getById(messageID)
        var found = false
        if(message.sender.publicWalletID == userID)
            found = true

        val messageStatus = messageStatusRepository.getAllByMessage(message)
        if(messageStatus != null) {
            for (msgStatus in messageStatus)
            {
                if(msgStatus.recipient.publicWalletID == userID)
                    found = true
            }
        }

        if(!found)
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.userNotPartOfAgreement)

        val messageList = ArrayList<Messages>()
        messageList.add(message)
        val messageResponseList = generateMessageResponseList(messageList)

        return ApiResponse(responseObject = GetMessageDetailResponse(messageResponseList[0]),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun sendMessage(userID:String, agreementID:UUID, sendMessageRequest: SendMessageRequest): ApiResponse<SendMessageResponse>{
        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
                        message = commonResponseErrorMessages.userDoesNotExist)

        if(!agreementsRepository.existsById(agreementID))
            return ApiResponse(status = ResponseStatus.FAILED,
                        message = commonResponseErrorMessages.agreementDoesNotExist)

        if(sendMessageRequest.Message.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED, message = "Message is empty")

        val user = userRepository.getById(userID)
        val agreement = agreementsRepository.getById(agreementID)
        val judges = judgesRepository.getAllByAgreement(agreement)

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
                tempMessageStatus.message = message
                tempMessageStatus = messageStatusRepository.save(tempMessageStatus)
                messageStatusList.add(tempMessageStatus)
            }
        }

        if(judges != null)
        {
            for(judge in judges)
            {
                val judgeUser = judge.judge
                if(!(usersInAgreement.contains(judgeUser)))
                {
                    var tempMessageStatus = MessageStatus(UUID.fromString("eebe3abc-b594-4a2f-a7dc-246bad26aaa5"))
                    tempMessageStatus.recipient = judgeUser
                    tempMessageStatus.message = message
                    tempMessageStatus = messageStatusRepository.save(tempMessageStatus)
                    messageStatusList.add(tempMessageStatus)
                }
            }
        }

        message.messageStatuses = messageStatusList

        return ApiResponse(responseObject = SendMessageResponse(message.messageID),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun setMessageAsRead(userID: String, messageID: UUID): ApiResponse<Objects>{
        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userDoesNotExist)

        if(!messagesRepository.existsById(messageID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.messageDoesNotExist)

        val user = userRepository.getById(userID)
        val message = messagesRepository.getById(messageID)
        val messageStatus = messageStatusRepository.getByRecipientAndMessage(user, message)?:
            return ApiResponse(status = ResponseStatus.FAILED,
                message = "User is not a recipient of the provided message")

        if(messageStatus.ReadDate != null)
            return ApiResponse(status = ResponseStatus.FAILED,
                    message = "Message has already been read")

        messageStatus.ReadDate = Date()

        messageStatusRepository.save(messageStatus)
        return ApiResponse(status = ResponseStatus.SUCCESSFUL)
    }

    fun getUnreadMessages(userID: String): ApiResponse<GetUnreadMessagesResponse>{
        val user = userRepository.getById(userID)

        val agreementList = agreementsRepository.getAllByUsersContaining(user)?:
            return ApiResponse(status = ResponseStatus.SUCCESSFUL)

        val messagesByAgreementResponseList = ArrayList<MessagesByAgreementResponse>()

        for(agreement in agreementList)
        {
            val messageList = messagesRepository.getAllByAgreements(agreement)
            if(messageList == null)
                messagesByAgreementResponseList.add(MessagesByAgreementResponse(agreement.ContractID))
            else
            {
                val unReadMessagesList = ArrayList<Messages>()
                for(msg in messageList)
                {
                    var messageStatus = messageStatusRepository.getByRecipientAndMessage(user, msg)
                    if(messageStatus != null) {
                        if (messageStatus.ReadDate == null) {
                            messageStatus = messageStatus.apply { ReadDate = Date() }
                            messageStatusRepository.save(messageStatus)
                            unReadMessagesList.add(msg)
                        }
                    }
                }
                val messageResponseList = generateMessageResponseList(unReadMessagesList)
                messagesByAgreementResponseList.add(
                    MessagesByAgreementResponse(AgreementID = agreement.ContractID,
                    Messages = messageResponseList))
            }
        }
        val getUnreadMessageResponse = GetUnreadMessagesResponse(messagesByAgreementResponseList)
        return ApiResponse(status = ResponseStatus.SUCCESSFUL,
        responseObject = getUnreadMessageResponse)
    }

}