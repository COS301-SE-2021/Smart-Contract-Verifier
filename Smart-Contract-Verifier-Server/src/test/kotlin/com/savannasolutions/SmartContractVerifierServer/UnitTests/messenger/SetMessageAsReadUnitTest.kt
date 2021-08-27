package com.savannasolutions.SmartContractVerifierServer.UnitTests.messenger

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.*
import com.savannasolutions.SmartContractVerifierServer.messenger.services.MessengerService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals

internal class SetMessageAsReadUnitTest {
    private val messagesRepository: MessagesRepository = mock()
    private val messageStatusRepository : MessageStatusRepository = mock()
    private val userRepository : UserRepository = mock()
    private val agreementsRepository: AgreementsRepository = mock()
    private val judgesRepository : JudgesRepository = mock()
    private val messengerService = MessengerService(messagesRepository,
                                                    messageStatusRepository,
                                                    userRepository,
                                                    agreementsRepository,
                                                    judgesRepository)

    private fun parameterizeSetMessageAsRead(recipientID: String,
                                             recipientExists: Boolean,
                                             messageExists: Boolean,
                                             ReadDate: Boolean,
                                             userMessageExists: Boolean): SetMessageAsReadResponse{
        //given
        val recipient = User(recipientID)
        val otherUser = User("other user")
        val userList = ArrayList<User>()
        userList.add(recipient)
        userList.add(otherUser)

        var agreement = Agreements(ContractID = UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            CreatedDate = Date(),
            MovedToBlockChain = false,).apply { users.add(recipient) }
        agreement = agreement.apply { users.add(otherUser) }

        var message = Messages(UUID.fromString("d34f7ef4-c109-426a-b20d-25f866e216f8"),
                                "TestMessage",Date())
        message = message.apply { sender = otherUser }
        message = message.apply { agreements = agreement }

        var messageStatus : MessageStatus = if(ReadDate)
            MessageStatus(UUID.fromString("f6d252fa-a4f8-439e-9ab3-e5f9e8225a78"),
                Date())
        else
            MessageStatus(UUID.fromString("f6d252fa-a4f8-439e-9ab3-e5f9e8225a78"))

        messageStatus = messageStatus.apply { this.recipient = recipient }
        messageStatus = messageStatus.apply { this.message = message }

        //when
        whenever(userRepository.existsById(recipientID)).thenReturn(recipientExists)
        whenever(userRepository.getById(recipientID)).thenReturn(recipient)
        whenever(messagesRepository.existsById(message.messageID)).thenReturn(messageExists)
        whenever(messagesRepository.getById(message.messageID)).thenReturn(message)
        if(userMessageExists)
            whenever(messageStatusRepository.getByRecipientAndMessage(recipient,message)).thenReturn(messageStatus)
        else
            whenever(messageStatusRepository.getByRecipientAndMessage(recipient,message)).thenReturn(null)
        whenever(messageStatusRepository.save(any<MessageStatus>())).thenReturn(messageStatus)

        return messengerService.setMessageAsRead(recipientID, message.messageID)
    }

    @Test
    fun `setMessageAsRead successful`(){
        //given

        //when
        val response = parameterizeSetMessageAsRead("userA",
                                                    recipientExists = true,
                                                    messageExists = true,
                                                    ReadDate = false,
                                                    userMessageExists = true)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `setMessageAsRead failed user does not exist`(){
        //given

        //when
        val response = parameterizeSetMessageAsRead("user A",
            recipientExists = false,
            messageExists = true,
            ReadDate = false,
            userMessageExists = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setMessageAsRead failed message does not exist`(){
        //given

        //when
        val response = parameterizeSetMessageAsRead("user A",
            recipientExists = true,
            messageExists = false,
            ReadDate = false,
            userMessageExists = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setMessageAsRead failed message has already marked as read`(){
        //given

        //when
        val response = parameterizeSetMessageAsRead("user b",
            recipientExists = true,
            messageExists = true,
            ReadDate = true,
            userMessageExists = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setMessageAsRead failed user is not part of the message`(){
        //given

        //when
        val response = parameterizeSetMessageAsRead("user a",
            recipientExists = true,
            messageExists = true,
            ReadDate = false,
            userMessageExists = false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }
}