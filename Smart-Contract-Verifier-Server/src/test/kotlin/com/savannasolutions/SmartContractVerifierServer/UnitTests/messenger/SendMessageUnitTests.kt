package com.savannasolutions.SmartContractVerifierServer.UnitTests.messenger

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.requests.SendMessageRequest
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.SendMessageResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.services.MessengerService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals

internal class SendMessageUnitTests {
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

        var message = Messages(
            UUID.fromString("b6a2832a-9485-4830-8940-8fa19338aca4"),
            messageData, Date()
        )
        message = message.apply { sender = user }

        var messageStatus = MessageStatus(
            UUID.fromString("714dde83-1a62-4213-9045-12e0e579101f"),
            Date()
        )
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
        return messengerService.sendMessage(sendingUserID, agreementID, SendMessageRequest(messageData))
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


}