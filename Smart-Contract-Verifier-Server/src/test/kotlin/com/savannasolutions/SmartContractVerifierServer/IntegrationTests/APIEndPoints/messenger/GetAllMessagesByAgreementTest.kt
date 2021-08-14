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
class GetAllMessagesByAgreementTest {
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

    private lateinit var userA: User
    private lateinit var agreementAUUID : UUID
    private lateinit var agreementBUUID : UUID
    private lateinit var messageA : Messages
    private lateinit var messageB : Messages

    @BeforeEach
    fun beforeEach()
    {
        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")

        val agreementA = Agreements(
            UUID.fromString("3c5657d6-e302-48d3-b9df-dcfccec97503"),
            "test agreement",
            "test description",
            CreatedDate = Date(),
            MovedToBlockChain = false)
        agreementAUUID = agreementA.ContractID

        val agreementB = Agreements(
            UUID.fromString("ffd886a9-f9c1-4189-92f1-0f4702ff5386"),
            "test agreement",
            "test description",
            CreatedDate = Date(),
            MovedToBlockChain = false)
        agreementBUUID = agreementB.ContractID

        messageA = Messages(UUID.fromString("16e54fea-d3e0-4547-b005-98bd68393504"),
                                "TestMessageA", Date())
        messageA.sender = userA
        messageA.agreements = agreementA
        val messagesStatusA = MessageStatus(UUID.fromString("93f47209-f5c1-4437-b3e1-099d467ef7db"), Date())
        messagesStatusA.message = messageA
        messagesStatusA.recipient = userB
        val messageStatusListA = ArrayList<MessageStatus>()
        messageStatusListA.add(messagesStatusA)
        messageA.messageStatuses = messageStatusListA

        messageB = Messages(UUID.fromString("92617bf4-02ad-418d-af7a-ba6d46136420"),
            "TestMessageB", Date())
        messageB.sender = userA
        messageB.agreements = agreementA
        val messagesStatusB = MessageStatus(UUID.fromString("85350e00-2dfe-4521-9da2-339deff4faa5"))
        messagesStatusB.message = messageB
        messagesStatusB.recipient = userB
        val messageStatusListB = ArrayList<MessageStatus>()
        messageStatusListB.add(messagesStatusB)
        messageB.messageStatuses = messageStatusListB


        val messageList = ArrayList<Messages>()
        messageList.add(messageA)
        messageList.add(messageB)

        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(agreementsRepository.existsById(agreementAUUID)).thenReturn(true)
        whenever(agreementsRepository.getById(agreementAUUID)).thenReturn(agreementA)
        whenever(agreementsRepository.getById(agreementBUUID)).thenReturn(agreementB)
        whenever(agreementsRepository.existsById(agreementBUUID)).thenReturn(true)
        whenever(messagesRepository.getAllByAgreements(agreementA)).thenReturn(messageList)
        for(msg in messageList)
        {
            whenever(messagesStatusRepository.getAllByMessage(msg)).thenReturn(msg.messageStatuses)
        }
    }

    private fun requestSender(rjson: String) : MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/messenger/get-all-messages-by-agreement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)).andReturn().response
    }

    @Test
    fun `GetAllMessagesByAgreementTest successful with messages`()
    {
        val rjson = "{\"AgreementID\" : \"${agreementAUUID.toString()}\",\"RequestingUser\" : \"${userA.publicWalletID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, messageA.messageID.toString())
        assertContains(response.contentAsString, messageB.messageID.toString())
        for(msg in messageA.messageStatuses!!)
            assertContains(response.contentAsString, msg.recipient.publicWalletID)
        for(msg in messageB.messageStatuses!!)
            assertContains(response.contentAsString, msg.recipient.publicWalletID)
    }

    @Test
    fun `GetAllMessagesByAgreementTest successful without messages`()
    {
        val rjson = "{\"AgreementID\" : \"${agreementBUUID.toString()}\",\"RequestingUser\" : \"${userA.publicWalletID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, "\"Messages\":[]")
    }

    @Test
    fun `GetAllMessagesByAgreementTest user does not exist`()
    {
        val rjson = "{\"AgreementID\" : \"${agreementBUUID.toString()}\",\"RequestingUser\" : \"\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `GetAllMessagesByAgreementTest failed agreement does not exist`()
    {
        val rjson = "{\"AgreementID\" : \"4285318d-c845-4324-9b8f-0ef915cca8dd\",\"RequestingUser\" : \"${userA.publicWalletID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }


}