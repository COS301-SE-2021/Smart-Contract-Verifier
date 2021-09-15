package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.evidence

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.LinkedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.UploadedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertContains
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "docs/api/delete/user/userID/agreement/agreementID/evidence/evidenceHash/linked")
class RemoveEvidenceLinkedTest {
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

    private lateinit var userA: User
    private lateinit var userB : User
    private lateinit var thirdParty: User
    private lateinit var agreement: Agreements
    private lateinit var evidence: Evidence

    @BeforeEach
    fun beforeEach(){
        evidenceConfig.initialise()
        //evidenceService.initialise()
        //given
        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")
        thirdParty = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B5")

        agreement = Agreements(
            UUID.fromString("377f66e7-5060-48f8-a44b-ae0bea405a5e"),
            CreatedDate = Date()
        )
        agreement.users.add(userA)
        agreement.users.add(userB)
        val linkedEvidence = LinkedEvidence(
            UUID.fromString("1981c189-afb4-431a-9fc5-d8e2e48b7110"),
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",)
        evidence = Evidence(
            UUID.fromString("7d793c67-10e8-419b-8137-be9758594184"),
            "aUseFulHash",
            EvidenceType.UPLOADED)
        evidence.user = userA
        evidence.contract = agreement
        evidence.evidenceUrl = linkedEvidence
        linkedEvidence.evidence = evidence

        //when
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(evidenceRepository.save(any<Evidence>())).thenReturn(evidence)
        whenever(evidenceRepository.existsById(evidence.evidenceId)).thenReturn(true)
        whenever(evidenceRepository.getById(evidence.evidenceId)).thenReturn(evidence)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(agreement.users.toList())
    }

    fun requestSender(userId: String,
                      agreementId: UUID,
                      evidenceID:UUID,
                      fieldDescriptors: ArrayList<FieldDescriptor>,
                      testName: String,): MockHttpServletResponse {
        val signingKey = Keys.hmacShaKeyFor("ThisIsATestKeySpecificallyForTests".toByteArray())
        val jwtToken = Jwts.builder()
            .setSubject(userId)
            .setExpiration(Date(System.currentTimeMillis() + 1080000))
            .signWith(signingKey)
            .compact()
        return mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/user/${userId}/agreement/${agreementId}/evidence/${evidenceID}/")
                .header("Authorization", "bearer $jwtToken")
        ).andDo(
            MockMvcRestDocumentation.document(testName,
            Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
            Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
            PayloadDocumentation.responseFields(
                fieldDescriptors
            ))).andReturn().response
    }

    @Test
    fun `Remove Evidence api test successful Linked`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiEmptyResponse())
        //end documentation
        val response = requestSender(userA.publicWalletID,
            agreement.ContractID,
            evidence.evidenceId,
            fieldDescriptor,
            "Remove Evidence api test successful Linked")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `Remove Evidence api test failed Agreement doesn't exist`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiFailedResponse())
        //end documentation
        val response = requestSender(userA.publicWalletID,
            UUID.fromString("ad02a8a0-0e91-41ab-9d71-9e6d67c0accc"),
            evidence.evidenceId,
            fieldDescriptor,
            "Remove Evidence api test failed Agreement doesn't exist")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `Remove Evidence api test failed User doesn't exist`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiFailedResponse())
        //end documentation
        val response = requestSender("0x4BBb50cd3d5FF41512f5e454E980EEEaeeb4e0bb",
            agreement.ContractID,
            evidence.evidenceId,
            fieldDescriptor,
            "Remove Evidence api test failed User doesn't exist")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `Remove Evidence api test failed Evidence doesn't exist`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiFailedResponse())
        //end documentation
        val response = requestSender(userA.publicWalletID,
            agreement.ContractID,
            UUID.fromString("f8d318fa-aa89-44e1-8023-a1a9ca823110"),
            fieldDescriptor,
            "Remove Evidence api test failed Evidence doesn't exist")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `Remove Evidence api test failed user not part of agreement`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiFailedResponse())
        //end documentation
        val response = requestSender(thirdParty.publicWalletID,
            agreement.ContractID,
            evidence.evidenceId,
            fieldDescriptor,
            "Remove Evidence api test failed user not part of agreement")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `Remove Evidence api test failed user not the owner of the evidence`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiFailedResponse())
        //end documentation
        val response = requestSender(userB.publicWalletID,
            agreement.ContractID,
            evidence.evidenceId,
            fieldDescriptor,
            "Remove Evidence api test failed user not the owner of the evidence")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
}