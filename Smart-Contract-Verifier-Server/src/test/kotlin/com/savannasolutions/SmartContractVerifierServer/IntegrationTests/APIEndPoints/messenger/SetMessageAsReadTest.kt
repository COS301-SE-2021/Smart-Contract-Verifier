package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.messenger

import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
class SetMessageAsReadTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var messagesRepository: MessagesRepository

    @MockBean
    lateinit var messagesStatusRepository: MessageStatusRepository

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var agreementsRepository: AgreementsRepository

    lateinit var message : Messages
    lateinit var userB : User

    @BeforeEach
    fun beforeEach()
    {
        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")

        val agreementA = Agreements(
            UUID.fromString("3c5657d6-e302-48d3-b9df-dcfccec97503"),
            "test agreement",
            "test description",
            CreatedDate = Date(),
            MovedToBlockChain = false)

        message = Messages(
            UUID.fromString("e6f884f4-8d9b-4efb-be31-c8ad5532f168"),
            "Test message", Date()
        )
        message.sender = userA
        message.agreements = agreementA

        val messageStatus = MessageStatus(
            UUID.fromString("eba04d3b-e671-4b00-9830-6d375f860048"))
        messageStatus.message = message
        messageStatus.recipient = userB

        val messageStatusList = ArrayList<MessageStatus>()
        messageStatusList.add(messageStatus)

        whenever(messagesRepository.existsById(message.messageID)).thenReturn(true)
        whenever(messagesRepository.getById(message.messageID)).thenReturn(message)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(messagesStatusRepository.getByRecipientAndMessage(userB,message)).thenReturn(messageStatus)
        whenever(messagesStatusRepository.getByRecipientAndMessage(userA,message)).thenReturn(null)

    }

    private fun requestSender(rjson: String) : MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/messenger/set-message-as-read")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)).andReturn().response
    }

    @Test
    fun `SetMessageAsReadTest successful`()
    {
        val rjson = "{\"MessageID\" : \"${message.messageID}\"," +
                        "\"RecipientID\" : \"${userB.publicWalletID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `SetMessageAsReadTest failed due to recipient is empty`()
    {
        val rjson = "{\"MessageID\" : \"${message.messageID}\"," +
                "\"RecipientID\" : \"\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `SetMessageAsReadTest failed due to user not existing`()
    {
        val rjson = "{\"MessageID\" : \"${message.messageID}\"," +
                "\"RecipientID\" : \"other people\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `SetMessageAsReadTest failed due to message not exists`()
    {
        val rjson = "{\"MessageID\" : \"638496e4-a37a-4095-9a6f-a5b8ce3ccffa\"," +
                "\"RecipientID\" : \"${userB.publicWalletID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

}