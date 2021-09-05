package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.messenger

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.requests.SendMessageRequest
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.GetAllMessagesByUserResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.SendMessageResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.util.*
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "docs/api/post/user/userID/agreement/agreementID/message")
class SendMessageTest {
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
    private lateinit var message : Messages


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

        val userList = ArrayList<User>()
        userList.add(userA)
        userList.add(userB)

        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(agreementsRepository.existsById(agreementAUUID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(agreementsRepository.getById(agreementAUUID)).thenReturn(agreementA)
        whenever(userRepository.getUsersByAgreementsContaining(agreementA)).thenReturn(userList)

        whenever(messagesRepository.save(any<Messages>())).thenReturn(message)
        whenever(messagesStatusRepository.save(any<MessageStatus>())).thenReturn(messageStatus)

    }

    private fun requestSender(rjson: String,
                              userID: String,
                              agreementID: UUID,
                              responseFieldDescriptors: ArrayList<FieldDescriptor>,
                              testName: String) : MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/user/${userID}/agreement/${agreementID}/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)).andDo(
            MockMvcRestDocumentation.document(
                testName,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(responseFieldDescriptors),
                PayloadDocumentation.requestFields(SendMessageRequest.request())
            )
        ).andReturn().response
    }

    @Test
    fun `SendMessage successful`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiResponse())
        fieldDescriptorResponse.addAll(SendMessageResponse.response())
        //End of documentation

        val rjson = "{\"Message\" : \"Test message\"}"

        val response = requestSender(rjson, userA.publicWalletID, agreementAUUID, fieldDescriptorResponse, "SendMessage successful")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, message.messageID.toString())
    }

    @Test
    fun `SendMessage failed due to user not existing`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"Message\" : \"Test message\"}"

        val response = requestSender(rjson, "other user", agreementAUUID, fieldDescriptorResponse, "SendMessage failed due to user not existing")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `SendMessage failed due to agreement not existing`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"Message\" : \"Test message\"}"

        val response = requestSender(rjson, userA.publicWalletID, UUID.fromString("eb558bea-389e-4e7b-afed-4987dbf37f85"),
            fieldDescriptorResponse,
            "SendMessage failed due to agreement not existing")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `SendMessage failed message is empty`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"Message\" : \"\"}"

        val response = requestSender(rjson,userA.publicWalletID, agreementAUUID,
            fieldDescriptorResponse, "SendMessage failed message is empty")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

}