package com.savannasolutions.SmartContractVerifierServer.UnitTests.messenger

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.requests.*
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class MessengerServiceTest {
    private val messagesRepository: MessagesRepository = mock()
    private val messageStatusRepository : MessageStatusRepository = mock()
    private val userRepository : UserRepository = mock()
    private val agreementsRepository: AgreementsRepository = mock()
    private val messengerService = MessengerService(messagesRepository,
                                                    messageStatusRepository,
                                                    userRepository,
                                                    agreementsRepository)

    private fun parameterizeGetAllMessagesByAgreement(userAddress: String,
                                                                agreementID: UUID,
                                                                userExist: Boolean,
                                                                agreementExist: Boolean,
                                                                messageList: List<Messages>? = null): GetAllMessagesByAgreementResponse
    {
        //given
        val user = User(userAddress)
        val otherUser = User("Other user")
        var agreement = Agreements(ContractID = agreementID,
                                    CreatedDate = Date(),
                                    MovedToBlockChain = false,)
        agreement = agreement.apply { users.add(user)}
        agreement = agreement.apply { users.add(otherUser)}

        //when

        whenever(userRepository.existsById(userAddress)).thenReturn(userExist)
        whenever(agreementsRepository.existsById(agreementID)).thenReturn(agreementExist)
        whenever(agreementsRepository.getById(agreementID)).thenReturn(agreement)
        whenever(messagesRepository.getAllByAgreements(agreement)).thenReturn(messageList)
        if(messageList != null)
        {
            for(msg in messageList)
            {
                var msgStatus = MessageStatus(UUID.randomUUID(), Date())
                msgStatus = msgStatus.apply { message = msg }
                if(msg.sender == user)
                    msgStatus = msgStatus.apply { recipient = otherUser }
                else
                    msgStatus = msgStatus.apply { recipient = user }
                val msgStatusList = ArrayList<MessageStatus>()
                msgStatusList.add(msgStatus)
                whenever(messageStatusRepository.getAllByMessage(msg)).thenReturn(msgStatusList)
            }
        }

        //then
        return messengerService.getAllMessagesByAgreement(GetAllMessagesByAgreementRequest(agreementID,userAddress))
    }

    private fun parameterizeGetAllMessagesByUser(userAddress: String,
                                                      userExist: Boolean,
                                                      messageListSending: List<Messages>? = null,
                                                      messageListReceivingStatus: List<MessageStatus>? = null,): GetAllMessagesByUserResponse
    {
        //given
        val user = User(userAddress)
        val otherUser = User("Other user")

        //when
        whenever(userRepository.existsById(userAddress)).thenReturn(userExist)
        whenever(userRepository.getById(userAddress)).thenReturn(user)
        whenever(messageStatusRepository.getAllByRecipient(user)).thenReturn(messageListReceivingStatus)
        whenever(messagesRepository.getAllBySender(user)).thenReturn(messageListSending)
        if(messageListSending != null)
        {
            for(msg in messageListSending)
            {
                var msgStatus = MessageStatus(UUID.randomUUID(), Date())
                msgStatus = msgStatus.apply { message = msg }
                if(msg.sender == user)
                    msgStatus = msgStatus.apply { recipient = otherUser }
                else
                    msgStatus = msgStatus.apply { recipient = user }
                val msgStatusList = ArrayList<MessageStatus>()
                msgStatusList.add(msgStatus)
                whenever(messageStatusRepository.getAllByMessage(msg)).thenReturn(msgStatusList)
            }
        }
        if(messageListReceivingStatus != null)
        {
            for(msgStatus in messageListReceivingStatus)
            {
                val msg = msgStatus.message
                val list = ArrayList<MessageStatus>()
                list.add(msgStatus)
                whenever(messagesRepository.getById(msg.messageID)).thenReturn(msg)
                whenever(messageStatusRepository.getAllByMessage(msg)).thenReturn(list)
            }
        }

        //then
        return messengerService.getAllMessagesByUser(GetAllMessagesByUserRequest(userAddress))
        }

    private fun parameterizeGetMessageDetail(messageID: UUID,
                                                messageExist: Boolean): GetMessageDetailResponse
    {
        //given
        var message = Messages(messageID,"Test Data", Date())

        val agreement = Agreements(ContractID = UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            CreatedDate = Date(),
            MovedToBlockChain = false,)

        message = message.apply { agreements = agreement }
        message = message.apply { sender = User("user A") }

        //when
        whenever(messagesRepository.existsById(messageID)).thenReturn(messageExist)
        whenever(messagesRepository.getById(messageID)).thenReturn(message)

        //then
        return messengerService.getMessageDetail(GetMessageDetailRequest(messageID))
    }

    private fun parameterizeSendMessage(messageData: String,
                                        sendingUserID: String,
                                        senderExists: Boolean,
                                        agreementID: UUID,
                                        agreementExist: Boolean): SendMessageResponse
    {
        //given
        val user = User(sendingUserID)
        val otherUser = User("other user")
        val userList = ArrayList<User>()
        userList.add(user)
        userList.add(otherUser)

        var agreement = Agreements(ContractID = UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            CreatedDate = Date(),
            MovedToBlockChain = false,).apply { users.add(user) }
        agreement = agreement.apply { users.add(otherUser) }

        var message = Messages(UUID.fromString("b6a2832a-9485-4830-8940-8fa19338aca4"),
                                messageData,Date())
        message = message.apply { sender = user }

        var messageStatus = MessageStatus(UUID.fromString("714dde83-1a62-4213-9045-12e0e579101f"),
                                            Date())
        messageStatus = messageStatus.apply { recipient = otherUser }

        //when
        whenever(userRepository.existsById(sendingUserID)).thenReturn(senderExists)
        whenever(userRepository.getById(sendingUserID)).thenReturn(user)
        whenever(agreementsRepository.existsById(agreementID)).thenReturn(agreementExist)
        whenever(agreementsRepository.getById(agreementID)).thenReturn(agreement)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(userList)
        whenever(messagesRepository.save(any<Messages>())).thenReturn(message)
        whenever(messageStatusRepository.save(any<MessageStatus>())).thenReturn(messageStatus)


        //then
        return messengerService.sendMessage(SendMessageRequest(sendingUserID, agreementID, messageData))
    }

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

        return messengerService.setMessageAsRead(SetMessageAsReadRequest(message.messageID,recipientID))
    }

    @Test
    fun `Successful test for getAllMessagesByAgreement without messages`(){
        //given

        //when
        val response = parameterizeGetAllMessagesByAgreement("userA",
                                                                UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
                                                                userExist = true,
                                                                agreementExist = true,)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.messages)
        assertTrue { response.messages!!.isEmpty()}
    }

    @Test
    fun `Successful test for getAllMessagesByAgreement with messages`(){
        //given
        val agreement = Agreements(ContractID = UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            CreatedDate = Date(),
            MovedToBlockChain = false,)

        val messageList = ArrayList<Messages>()
        var msg1 = Messages(UUID.fromString("3d63ca50-5584-460a-881a-24c215f0a5e2"),
                            "Message 1", Date())
        msg1 = msg1.apply { sender = User("7b67f0f4-6433-4a72-b467-c6ddb9dd772a") }
        msg1 = msg1.apply { agreements = agreement }

        var msg2 = Messages(UUID.fromString("013e7a05-c831-400c-a43d-507018db7b2c"),
            "Message 2", Date())
        msg2 = msg2.apply { sender = User("7b67f0f4-6433-4a72-b467-c6ddb9dd772a") }
        msg2 = msg2.apply { agreements = agreement }

        messageList.add(msg1)
        messageList.add(msg2)

        //when
        val response = parameterizeGetAllMessagesByAgreement("userA",
            UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            userExist = true,
            agreementExist = true,
            messageList)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.messages)
        assertFalse{response.messages!!.isEmpty()}
    }

    @Test
    fun `getAllMessagesByAgreement failed user does not exist`(){
        //given

        //when
        val response = parameterizeGetAllMessagesByAgreement("userA",
            UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            userExist = false,
            agreementExist = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAllMessagesByAgreement failed agreement does not exist`(){
        //given

        //when
        val response = parameterizeGetAllMessagesByAgreement("userA",
            UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            userExist = true,
            agreementExist = false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAllMessagesByUser successful both receivedMessagesList and sentMessageList is null`()
    {
        //given

        //when
        val response = parameterizeGetAllMessagesByUser("userA",
                                                        true,)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.messages)
        assertTrue(response.messages!!.isEmpty())
    }

    @Test
    fun `getAllMessagesByUser successful where receivedMessagesList is not null but sentMessageList is null`(){
        //given
        val agreement = Agreements(ContractID = UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            CreatedDate = Date(),
            MovedToBlockChain = false,)

        var msg1 = Messages(UUID.fromString("015f88d6-22a6-43ee-a147-35fdc566465b"),
            "TestMessage", Date()).apply { this.agreements = agreement }

        msg1 = msg1.apply { sender = User("Other user") }

        var msgStat1 = MessageStatus(UUID.fromString("015f88d6-22a6-43ee-a147-35fdc566465b"),
                                    Date())

        msgStat1 = msgStat1.apply { message = msg1 }
        msgStat1 = msgStat1.apply { recipient = User("userA") }

        var msg2 = Messages(UUID.fromString("76a7c220-7ee8-4517-96fb-e9f2f2c4bb73"),
            "TestMessage2", Date()).apply { this.agreements = agreement }

        msg2 = msg2.apply { sender = User("Other user") }

        var msgStat2 = MessageStatus(UUID.fromString("76a7c220-7ee8-4517-96fb-e9f2f2c4bb73"),
                                        Date())
        msgStat2 = msgStat2.apply { message = msg2}
        msgStat2 = msgStat2.apply { recipient = User("userA") }

        val msgStatusList = ArrayList<MessageStatus>()
        msgStatusList.add(msgStat1)
        msgStatusList.add(msgStat2)


        //when
        val response = parameterizeGetAllMessagesByUser("userA", true, messageListReceivingStatus = msgStatusList)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.messages)
        assertFalse { response.messages!!.isEmpty() }
    }

    @Test
    fun `getAllMessagesByUser successful where sendMessages is not null but receivedMessage are`(){
        //given
        val agreement = Agreements(ContractID = UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            CreatedDate = Date(),
            MovedToBlockChain = false,)

        var msg1 = Messages(UUID.fromString("53298477-791e-43a5-a45f-7237e1c3531a"),
                            "Message 1", Date())
        var msgStat1 = MessageStatus(UUID.fromString("7ef51978-d3c8-486b-9bfa-f6c7691c39b4"))
        msgStat1 = msgStat1.apply { recipient = User("other user") }
        msgStat1 = msgStat1.apply { message = msg1 }
        msg1 = msg1.apply { sender = User("user A") }
        msg1 = msg1.apply { agreements = agreement }

        val msgStatList1 = ArrayList<MessageStatus>()
        msgStatList1.add(msgStat1)
        msg1 = msg1.apply { messageStatuses = msgStatList1 }

        var msg2 = Messages(UUID.fromString("54298477-791e-43a5-a45f-7237e1c3531a"),
            "Message 2", Date())
        var msgStat2 = MessageStatus(UUID.fromString("6ef51978-d3c8-486b-9bfa-f6c7691c39b4"))
        msgStat2 = msgStat2.apply { recipient = User("other user") }
        msgStat2 = msgStat2.apply { message = msg2 }
        msg2 = msg2.apply { sender = User("user A") }
        val msgStatList2 = ArrayList<MessageStatus>()
        msgStatList2.add(msgStat2)
        msg2 = msg2.apply { messageStatuses = msgStatList1 }
        msg2 = msg2.apply { agreements = agreement }

        val senderList = ArrayList<Messages>()
        senderList.add(msg1)
        senderList.add(msg2)

        //when
        val response = parameterizeGetAllMessagesByUser(msg1.sender.publicWalletID,
                                                        true,
                                                        senderList)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.messages)
        assertFalse { response.messages!!.isEmpty() }
    }

    @Test
    fun `getMessagesByUser where both sendingMessages and receiving messages are not null`(){
        //Given
        val agreement = Agreements(ContractID = UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            CreatedDate = Date(),
            MovedToBlockChain = false,)

        //Receiving messages
        var msg1R = Messages(UUID.fromString("015f88d6-22a6-43ee-a147-35fdc566465b"),
            "TestMessage", Date()).apply { this.agreements = agreement }

        msg1R = msg1R.apply { sender = User("Other user") }

        var msgStat1R = MessageStatus(UUID.fromString("015f88d6-22a6-43ee-a147-35fdc566465b"),
            Date())

        msgStat1R = msgStat1R.apply { message = msg1R }
        msgStat1R = msgStat1R.apply { recipient = User("userA") }

        var msg2R = Messages(UUID.fromString("76a7c220-7ee8-4517-96fb-e9f2f2c4bb73"),
            "TestMessage2", Date()).apply { this.agreements = agreement }

        msg2R = msg2R.apply { sender = User("Other user") }

        var msgStat2R = MessageStatus(UUID.fromString("76a7c220-7ee8-4517-96fb-e9f2f2c4bb73"),
            Date())
        msgStat2R = msgStat2R.apply { message = msg2R}
        msgStat2R = msgStat2R.apply { recipient = User("userA") }

        val msgStatusListR = ArrayList<MessageStatus>()
        msgStatusListR.add(msgStat1R)
        msgStatusListR.add(msgStat2R)

        //Sending
        var msg1S = Messages(UUID.fromString("53298477-791e-43a5-a45f-7237e1c3531a"),
            "Message 1", Date())
        var msgStat1S = MessageStatus(UUID.fromString("7ef51978-d3c8-486b-9bfa-f6c7691c39b4"))
        msgStat1S = msgStat1S.apply { recipient = User("other user") }
        msgStat1S = msgStat1S.apply { message = msg1S }
        msg1S = msg1S.apply { sender = User("userA") }
        msg1S = msg1S.apply { agreements = agreement }

        val msgStatList1S = ArrayList<MessageStatus>()
        msgStatList1S.add(msgStat1S)
        msg1S = msg1S.apply { messageStatuses = msgStatList1S }

        var msg2S = Messages(UUID.fromString("54298477-791e-43a5-a45f-7237e1c3531a"),
            "Message 2", Date())
        var msgStat2S = MessageStatus(UUID.fromString("6ef51978-d3c8-486b-9bfa-f6c7691c39b4"))
        msgStat2S = msgStat2S.apply { recipient = User("other user") }
        msgStat2S = msgStat2S.apply { message = msg2S }
        msg2S = msg2S.apply { sender = User("userA") }
        val msgStatList2S = ArrayList<MessageStatus>()
        msgStatList2S.add(msgStat2S)
        msg2S = msg2S.apply { messageStatuses = msgStatList1S }
        msg2S = msg2S.apply { agreements = agreement }

        val senderListS = ArrayList<Messages>()
        senderListS.add(msg1S)
        senderListS.add(msg2S)

        //When
        val response = parameterizeGetAllMessagesByUser("userA",
                                                            true,
                                                            senderListS,
                                                            msgStatusListR)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.messages)
        assertFalse { response.messages!!.isEmpty() }
    }

    @Test
    fun `getAllMessagesByUser failure where user string is empty`(){
        //given

        //when
        val response = parameterizeGetAllMessagesByUser("", true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAllMessagesByUser failure where user does not exist`(){
        //given

        //when
        val response = parameterizeGetAllMessagesByUser("userA", false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getMessageDetail success`(){
        //given

        //when
        val response = parameterizeGetMessageDetail(UUID.fromString("887372ab-d2e9-45a7-af1f-d2a6924c1466"), true)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getMessageDetail failure message does not exist`(){
        //given

        //when
        val response = parameterizeGetMessageDetail(UUID.fromString("887372ab-d2e9-45a7-af1f-d2a6924c1466"), false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sendMessage successful`(){
        //given

        //when
        val response = parameterizeSendMessage("Test",
                                            "userA",
                                            true,
                                                UUID.fromString("9f56bc45-bb70-4179-9c6c-ec5031169a26"),
                                                true)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `sendMessage failure user does not exist`(){
        //given

        //when
        val response = parameterizeSendMessage("Test",
            "user A",
            false,
            UUID.fromString("9f56bc45-bb70-4179-9c6c-ec5031169a26"),
            true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sendMessage failure agreement does not exist`(){
        //given

        //when
        val response = parameterizeSendMessage("Test",
            "user A",
            true,
            UUID.fromString("9f56bc45-bb70-4179-9c6c-ec5031169a26"),
            false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sendMessage failure message data is empty`(){
        //given

        //when
        val response = parameterizeSendMessage("",
            "user A",
            true,
            UUID.fromString("9f56bc45-bb70-4179-9c6c-ec5031169a26"),
            true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
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
    fun `setMessageAsRead failed recipientID is empty`(){
        //given

        //when
        val response = parameterizeSetMessageAsRead("",
            recipientExists = true,
            messageExists = true,
            ReadDate = false,
            userMessageExists = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
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