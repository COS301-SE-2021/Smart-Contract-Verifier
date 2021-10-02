package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.evidence

import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
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
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path
import kotlin.test.assertEquals

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
    var evidenceID : UUID = UUID.fromString("f8d318fa-aa89-44e1-8023-a1a9ca823110")

    @BeforeEach
    fun beforeEach() {
        //evidenceConfig.initialise()
        //evidenceService.initialise()
        //given
        user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        otherUser = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")

        agreement = Agreements(
            UUID.fromString("377f66e7-5060-48f8-a44b-ae0bea405a5e"),
            CreatedDate = Date()
        )
        agreement.users.add(user)

        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
            "testFile.txt",
            "text/Plain")

        evidence = Evidence(evidenceID,
            "A usefull hash",
            EvidenceType.UPLOADED)

        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(agreement.users.toList())
        whenever(evidenceRepository.save(any<Evidence>())).thenReturn(evidence)
        whenever(evidenceRepository.existsById(evidence.evidenceId)).thenReturn(true)
        whenever(evidenceRepository.getById(evidence.evidenceId)).thenReturn(evidence)

        val response = upload(user.publicWalletID,
            agreement.ContractID,
            file)

        //val responseValues = ObjectMapper().readTree(response.contentAsString)

        val uploadedEvidence = UploadedEvidence(evidenceID, agreement.ContractID.toString()+user.publicWalletID+"testFile.txt", "text/plain", "testFile.txt")
        //when
        evidence.uploadedEvidence = uploadedEvidence
        uploadedEvidence.evidence = evidence
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
                .header("Authorization", "bearer ${generateToken(userId)}")
        ).andReturn().response
    }

    @Test
    fun `download evidence api test success`() {
        val response = requestSender(
            user.publicWalletID,
            agreement.ContractID,
            evidenceID,
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
            evidenceID,
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
                .header("Authorization", "bearer ${generateToken(userID)}")
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
   fun generateToken(userID: String): String? {
       val signingKey = Keys.hmacShaKeyFor("ThisIsATestKeySpecificallyForTests".toByteArray())
       return Jwts.builder()
           .setSubject(userID)
           .setExpiration(Date(System.currentTimeMillis() + 1080000))
           .signWith(signingKey)
           .compact()
   }
}