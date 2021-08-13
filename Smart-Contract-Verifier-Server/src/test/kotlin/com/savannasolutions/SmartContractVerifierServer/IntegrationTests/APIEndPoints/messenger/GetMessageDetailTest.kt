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
class GetMessageDetailTest {
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

    @BeforeEach
    fun beforeEach()
    {
        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")

        val agreementA = Agreements(
            UUID.fromString("3c5657d6-e302-48d3-b9df-dcfccec97503"),
            "test agreement",
            "test description",
            CreatedDate = Date(),
            MovedToBlockChain = false)

        message = Messages(UUID.fromString("e6f884f4-8d9b-4efb-be31-c8ad5532f168"),
                            "Test message", Date())
        message.sender = userA
        message.agreements = agreementA

        val messageStatus = MessageStatus(UUID.fromString("eba04d3b-e671-4b00-9830-6d375f860048"),
                                            Date())
        messageStatus.message = message
        messageStatus.recipient = userB

        val messageStatusList = ArrayList<MessageStatus>()
        messageStatusList.add(messageStatus)

        whenever(messagesRepository.existsById(message.messageID)).thenReturn(true)
        whenever(messagesRepository.getById(message.messageID)).thenReturn(message)
        whenever(messagesStatusRepository.getAllByMessage(message)).thenReturn(messageStatusList)
    }

    private fun requestSender(rjson: String) : MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/messenger/get-message-detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)).andReturn().response
    }

    @Test
    fun `GetMessageDetail successful`()
    {
        val rjson = "{\"MessageID\" : \"${message.messageID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, message.messageID.toString())
        assertContains(response.contentAsString, message.message)
    }

    @Test
    fun `GetMessageDetail failed message does not exist`()
    {
        val rjson = "{\"MessageID\" : \"689de7e1-103a-4131-8a5f-000d46b800ae\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

}