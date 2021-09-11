package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.evidence

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.LinkedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.requests.LinkEvidenceRequest
import com.savannasolutions.SmartContractVerifierServer.evidence.responses.LinkEvidenceResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.requests.SendMessageRequest
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
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "docs/api/post/user/userID/agreement/agreementID/evidence/link")
class LinkEvidenceTest {
    @Autowired
    lateinit var mockMvc : MockMvc

    @MockBean
    lateinit var agreementsRepository : AgreementsRepository

    @MockBean
    lateinit var userRepository : UserRepository

    @MockBean
    lateinit var evidenceRepository : EvidenceRepository

    @MockBean
    lateinit var linkedEvidenceRepository: LinkedEvidenceRepository

    @MockBean
    lateinit var uploadedEvidenceRepository: UploadedEvidenceRepository

    @MockBean
    lateinit var judgesRepository: JudgesRepository

    private val evidenceConfig = EvidenceConfig("TEST")

    private lateinit var user: User
    private lateinit var otherUser : User
    private lateinit var agreement: Agreements

    @BeforeEach
    fun beforeEach(){
        //given
        evidenceConfig.initialise()

        user = User("TestUser1")
        otherUser = User("TestUser2")
        agreement = Agreements(UUID.fromString("377f66e7-5060-48f8-a44b-ae0bea405a5e"),
            "TestAgreement",
            CreatedDate = Date())
        agreement.users.add(user)
        val evidence = Evidence("LinkedHash", EvidenceType.LINKED)
        evidence.user = user
        evidence.contract = agreement
        val linkedEvidence = LinkedEvidence(UUID.fromString("bfe3e6c6-da45-4f89-bb99-06a03ebb3e2a"),
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        evidence.evidenceUrl = linkedEvidence
        linkedEvidence.evidence = evidence
        //when
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(evidenceRepository.save(any<Evidence>())).thenReturn(evidence)
    }

    fun requestSender(userId: String,
                      agreementId: UUID,
                      rjson: String,
                      testName: String,
                      responseFieldDescriptors: ArrayList<FieldDescriptor>,): MockHttpServletResponse{
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/user/${userId}/agreement/${agreementId}/evidence/link")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson))
                .andDo(MockMvcRestDocumentation.document(
                    testName,
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                    PayloadDocumentation.responseFields(responseFieldDescriptors),
                    PayloadDocumentation.requestFields(LinkEvidenceRequest.request())
                )
        ).andReturn().response
    }

    @Test
    fun `Link Evidence api test successfully linked`(){
        //documentation
        val fieldDescriptors = ArrayList<FieldDescriptor>()
        fieldDescriptors.addAll(ApiResponse.apiEmptyResponse())
        //end documentation

        val json = "{\"EvidenceUrl\" : \"https://www.youtube.com/watch?v=dQw4w9WgXcQ\"}"
        val response = requestSender(user.publicWalletID,
            agreement.ContractID,
            json,
            "Link Evidence API successful test",
            fieldDescriptors)
        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `Link Evidence api test failed agreement doesn't exist`(){
        //documentation
        val fieldDescriptors = ArrayList<FieldDescriptor>()
        fieldDescriptors.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptors.addAll(LinkEvidenceResponse.response())
        //end documentation

        val json = "{\"EvidenceUrl\" : \"https://www.youtube.com/watch?v=dQw4w9WgXcQ\"}"
        val response = requestSender(user.publicWalletID,
            UUID.fromString("45e7c90d-8c50-43ce-830b-3079fcf5905b"),
            json,
            "Link Evidence API successful test",
            fieldDescriptors)
        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `Link Evidence api test failed user doesn't exist`(){
        //documentation
        val fieldDescriptors = ArrayList<FieldDescriptor>()
        fieldDescriptors.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptors.addAll(LinkEvidenceResponse.response())
        //end documentation

        val json = "{\"EvidenceUrl\" : \"https://www.youtube.com/watch?v=dQw4w9WgXcQ\"}"
        val response = requestSender("invalidUser",
            agreement.ContractID,
            json,
            "Link Evidence API successful test",
            fieldDescriptors)
        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `Link Evidence api test failed user isn't in agreement`(){
        //documentation
        val fieldDescriptors = ArrayList<FieldDescriptor>()
        fieldDescriptors.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptors.addAll(LinkEvidenceResponse.response())
        //end documentation

        val json = "{\"EvidenceUrl\" : \"https://www.youtube.com/watch?v=dQw4w9WgXcQ\"}"
        val response = requestSender(otherUser.publicWalletID,
            agreement.ContractID,
            json,
            "Link Evidence API successful test",
            fieldDescriptors)
        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
}