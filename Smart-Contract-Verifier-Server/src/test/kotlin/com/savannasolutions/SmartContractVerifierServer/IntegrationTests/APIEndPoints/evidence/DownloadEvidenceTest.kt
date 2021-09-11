package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.evidence

import com.fasterxml.jackson.databind.ObjectMapper
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.UploadedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.responses.DownloadEvidenceResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.json.JSONArray
import org.json.JSONObject
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
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.util.*
import javax.swing.JOptionPane
import kotlin.io.path.Path
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "docs/api/get/user/userID/agreement/agreementID/evidence/evidenceID/download")
class DownloadEvidenceTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var agreementsRepository: AgreementsRepository

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var evidenceRepository: EvidenceRepository

    @MockBean
    lateinit var linkedEvidenceRepository: LinkedEvidenceRepository

    @MockBean
    lateinit var uploadedEvidenceRepository: UploadedEvidenceRepository

    @MockBean
    lateinit var judgesRepository: JudgesRepository

    private lateinit var user: User
    private lateinit var otherUser: User
    private lateinit var agreement: Agreements
    private lateinit var evidence: Evidence

    @BeforeEach
    fun beforeEach() {
        //evidenceConfig.initialise()
        //evidenceService.initialise()
        //given
        user = User("test user")
        otherUser = User("other")

        agreement = Agreements(
            UUID.fromString("377f66e7-5060-48f8-a44b-ae0bea405a5e"),
            CreatedDate = Date()
        )
        agreement.users.add(user)

        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
            "testFile.txt",
            "text/Plain")

        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(agreement.users.toList())

        val response = upload(user.publicWalletID,
            agreement.ContractID,
            file)

        val responseValues = ObjectMapper().readTree(response.contentAsString)

        evidence = Evidence(UUID.fromString(responseValues.get("ResponseObject").get("EvidenceHash").asText()),
            "A usefull hash",
            EvidenceType.UPLOADED)
        val uploadedEvidence = UploadedEvidence(UUID.fromString(responseValues.get("ResponseObject").get("EvidenceHash").asText()), agreement.ContractID.toString()+user.publicWalletID+"testFile.txt", "text/plain", "testFile.txt")
        //when
        evidence.uploadedEvidence = uploadedEvidence
        uploadedEvidence.evidence = evidence

        whenever(evidenceRepository.save(any<Evidence>())).thenReturn(evidence)
        whenever(evidenceRepository.existsById(evidence.evidenceId.toString())).thenReturn(true)
        whenever(evidenceRepository.getById(evidence.evidenceId.toString())).thenReturn(evidence)

    }

    private fun convertTextFileToMultipartFile(filePath: String, fileName: String, mimeType: String): MultipartFile {
        val path = Path(filePath + fileName)
        var byteArr: ByteArray? = null
        try {
            byteArr = Files.readAllBytes(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return MockMultipartFile("uploadEvidence", fileName, mimeType, byteArr)
    }

    fun requestSender(
        userId: String,
        agreementId: UUID,
        evidenceId: UUID,
    ): MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .get("/user/${userId}/agreement/${agreementId}/evidence/${evidenceId}/download")
        ).andReturn().response
    }

    @Test
    fun `download evidence api test success`() {
        val response = requestSender(
            user.publicWalletID,
            agreement.ContractID,
            evidence.evidenceId,
        )

        assertEquals(response.contentAsString, "hello")
        //assertTrue(response.file!!.isFile)
    }

    @Test
    fun `download evidence api test failed agreement doesn't exist`() {
        val response = requestSender(
            user.publicWalletID,
            UUID.fromString("b208e88b-5c1a-4ddd-8da8-5884f83173e5"),
            evidence.evidenceId,
        )

        assertEquals(response.status, 404)
    }

    @Test
    fun `download evidence api test failed user doesn't exist`() {
        val response = requestSender(
            "invalid user",
            agreement.ContractID,
            evidence.evidenceId,
        )

        assertEquals(response.status, 404)
    }

    @Test
    fun `download evidence api test failed evidence doesn't exist`() {
        val response = requestSender(
            user.publicWalletID,
            agreement.ContractID,
            UUID.fromString("b208e88b-5c1a-4ddd-8da8-5884f83173e5"),
        )

        assertEquals(response.status, 404)
    }

    @Test
    fun `download evidence api test failed user not part of agreement`() {
        val response = requestSender(
            otherUser.publicWalletID,
            agreement.ContractID,
            evidence.evidenceId,
        )

        assertEquals(response.status, 404)
    }


     fun upload(userID: String,
                              agreementID: UUID,
                              file: MultipartFile,): MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders.multipart("/user/${userID}/agreement/${agreementID}/evidence/upload").file(file as MockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andReturn().response
    }

   /*
    fun `UploadEvidence api test successful text file`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptor.addAll(UploadEvidenceResponse.response())
        //End of documentation

        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
                                                "testFile.txt",
                                                "text/Plain")

        upload(user.publicWalletID,
                                    agreement.ContractID,
                                    file,
                                    fieldDescriptor,
                                    "UploadEvidence api test successful text file")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }*/
}