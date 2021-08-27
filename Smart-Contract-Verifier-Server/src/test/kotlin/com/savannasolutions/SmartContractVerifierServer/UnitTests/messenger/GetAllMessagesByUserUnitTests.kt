package com.savannasolutions.SmartContractVerifierServer.UnitTests.messenger

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.GetAllMessagesByUserResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.services.MessengerService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class GetAllMessagesByUserUnitTests {
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

    private fun parameterizeGetAllMessagesByUser(userAddress: String,
                                                 userExist: Boolean,
                                                 messageListSending: List<Messages>? = null,
                                                 messageListReceivingStatus: List<MessageStatus>? = null,):
            ApiResponse<GetAllMessagesByUserResponse>
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
        return messengerService.getAllMessagesByUser(userAddress)
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
        assertNotNull(response.responseObject)
        assertNotNull(response.responseObject!!.messages)
        assertTrue(response.responseObject!!.messages!!.isEmpty())
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
        assertNotNull(response.responseObject)
        assertNotNull(response.responseObject!!.messages)
        assertFalse { response.responseObject!!.messages!!.isEmpty() }
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
        assertNotNull(response.responseObject)
        assertNotNull(response.responseObject!!.messages)
        assertFalse { response.responseObject!!.messages!!.isEmpty() }
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
        assertNotNull(response.responseObject)
        assertNotNull(response.responseObject!!.messages)
        assertFalse { response.responseObject!!.messages!!.isEmpty() }
    }

    @Test
    fun `getAllMessagesByUser failure where user does not exist`(){
        //given

        //when
        val response = parameterizeGetAllMessagesByUser("userA", false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }


}