package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.messenger

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.GetUnreadMessagesResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
@AutoConfigureRestDocs(outputDir = "docs/api/get/user/userid/message/unread")
class GetUnreadMessagesTest {
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

    private lateinit var agreementAUUID : UUID
    private lateinit var agreementBUUID : UUID
    private lateinit var userAWalletID: String
    private lateinit var userBWalletID: String
    private lateinit var messageAID: UUID
    private lateinit var messageBID: UUID

    @BeforeEach
    fun beforeEach()
    {

        val userA = User("userA")
        userAWalletID = userA.publicWalletID
        val userB = User("userB")
        userBWalletID = userB.publicWalletID

        val agreementA = Agreements(
            ContractID = UUID.fromString("1060e965-ce97-45c9-bece-88ec22b88f86"),
            CreatedDate = Date()).apply { users.add(userA) }.apply { users.add(userB) }
        agreementAUUID = agreementA.ContractID

        val agreementB = Agreements(
            ContractID = UUID.fromString("432cb7d7-a9c5-468c-8c7a-6cc0634b041a"),
            CreatedDate = Date()).apply { users.add(userB) }.apply { users.add(userA) }
        agreementBUUID = agreementB.ContractID

        val messageA = Messages(UUID.fromString("0291184c-036a-457c-a37f-1684c69bd9bb"),
            "Test message", Date()).apply { sender = userA }.apply { agreements = agreementA }

        val messageB = Messages(UUID.fromString("4718af9e-d0f6-4677-a639-79a6fe6cff7f"),
            "Test message 2", Date()).apply { sender = userB }.apply { agreements = agreementB }

        messageAID = messageA.messageID
        messageBID = messageB.messageID

        val messageStatusA =
            MessageStatus(UUID.fromString("8c5c8aa5-eac6-4dfe-90a2-d0be8b6005a3")).apply { recipient = userB }.apply { message = messageA }

        val messageStatusB =
            MessageStatus(UUID.fromString("f1526d67-a0af-4c08-bca2-6285d631c0a5"),
                Date()).apply { recipient = userA }.apply { message = messageB }

        val agreementSet = mutableSetOf<Agreements>()
        agreementSet.add(agreementA)
        agreementSet.add(agreementB)

        val messageList = ArrayList<Messages>()
        messageList.add(messageA)
        messageList.add(messageB)

        //val messageAStatusDone = messageStatusA
        //messageAStatusDone.ReadDate = Date()

        val messageStatusList = ArrayList<MessageStatus>()
        messageStatusList.add(messageStatusA)

        //when
        whenever(userRepository.getById(userAWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userBWalletID)).thenReturn(userB)
        whenever(agreementsRepository.getById(agreementAUUID)).thenReturn(agreementA)
        whenever(agreementsRepository.getById(agreementBUUID)).thenReturn(agreementB)
        whenever(agreementsRepository.getAllByUsersContaining(userB)).thenReturn(agreementSet)
        whenever(messagesRepository.getAllByAgreements(agreementA)).thenReturn(messageList)
        whenever(messagesStatusRepository.getByRecipientAndMessage(userB, messageA)).thenReturn(messageStatusA)
        whenever(messagesStatusRepository.getByRecipientAndMessage(userB, messageB)).thenReturn(messageStatusB)
        //whenever(messageStatusRepository.save(messageStatusA)).thenReturn(messageAStatusDone)
        whenever(messagesStatusRepository.getAllByMessage(messageA)).thenReturn(messageStatusList)
    }

    fun requestSender(responseFieldDescriptors: ArrayList<FieldDescriptor>,
                      testName: String): MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.get("/user/${userBWalletID}/message/unread")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
            MockMvcRestDocumentation.document(
                testName,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(responseFieldDescriptors)
            )
        ).andReturn().response
    }

    @Test
    fun `GetUnreadMessages successful`()
    {
        //Documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiResponse())
        fieldDescriptorResponse.addAll(GetUnreadMessagesResponse.response())
        //End of documentation

        //given

        //when
        val response = requestSender(fieldDescriptorResponse,"GetUnreadMessages successful")

        //then
        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, agreementAUUID.toString())
        assertContains(response.contentAsString, messageAID.toString())
    }

}