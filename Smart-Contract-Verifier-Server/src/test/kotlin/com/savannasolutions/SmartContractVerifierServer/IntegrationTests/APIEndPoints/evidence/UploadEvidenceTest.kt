package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.evidence

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.UploadedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.responses.UploadEvidenceResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.util.*
import kotlin.collections.ArrayList
import kotlin.io.path.Path
import kotlin.test.assertContains
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "docs/api/post/user/userID/agreement/agreementID/evidence/upload")
class UploadEvidenceTest {
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
        evidenceConfig.initialise()
        //evidenceService.initialise()
        //given
        user = User("0x7Ea7EA8D709B02444128e8b4d8C38d00842e77C3")
        otherUser = User("0xF9276468FA51422cD528BEAcAb7aB548Ba71Cf17")
        agreement = Agreements(
            UUID.fromString("377f66e7-5060-48f8-a44b-ae0bea405a5e"),
            CreatedDate = Date()
        )
        agreement.users.add(user)
        val uploadedEvidence = UploadedEvidence(
            UUID.fromString("1981c189-afb4-431a-9fc5-d8e2e48b7110"),
            "testFile",
            "txt",
            "testFile")
        val evidence = Evidence(
            UUID.fromString("7d793c67-10e8-419b-8137-be9758594184"),
            "aUseFulHash",
            EvidenceType.UPLOADED)
        evidence.user = user
        evidence.contract = agreement
        evidence.uploadedEvidence = uploadedEvidence
        uploadedEvidence.evidence = evidence
        //when
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(evidenceRepository.save(any<Evidence>())).thenReturn(evidence)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(agreement.users.toList())
    }

    private fun convertTextFileToMultipartFile(filePath:String, fileName: String, mimeType: String): MultipartFile {
        val path = Path(filePath+fileName)
        var byteArr : ByteArray? = null
        try{
            byteArr = Files.readAllBytes(path)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
        return MockMultipartFile("uploadEvidence", fileName, mimeType, byteArr)
    }

    private fun requestSender(userID: String,
                              agreementID: UUID,
                              file: MultipartFile,
                              responseFieldDescriptors: ArrayList<FieldDescriptor>,
                              testName: String) : MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.multipart("/user/${userID}/agreement/${agreementID}/evidence/upload").file(file as MockMultipartFile)
                .header("Authorization", "bearer ${generateToken(userID)}")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andDo(
            MockMvcRestDocumentation.document(
                testName,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(
                    responseFieldDescriptors
                ),
            )
        ).andReturn().response
    }

    @Test
    fun `UploadEvidence api test successful text file`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptor.addAll(UploadEvidenceResponse.response())
        //End of documentation

        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
                                                "testFile.txt",
                                                "text/Plain")

        val response = requestSender(user.publicWalletID,
                                    agreement.ContractID,
                                    file,
                                    fieldDescriptor,
                                    "UploadEvidence api test successful text file")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `UploadEvidence api test successful pdf`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptor.addAll(UploadEvidenceResponse.response())
        //End of documentation

        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
            "testFile.pdf",
            "application/pdg")

        val response = requestSender(user.publicWalletID,
            agreement.ContractID,
            file,
            fieldDescriptor,
            "UploadEvidence api test successful pdf")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `UploadEvidence api test successful png`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptor.addAll(UploadEvidenceResponse.response())
        //End of documentation

        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
            "testFile.png",
            "image/png")

        val response = requestSender(user.publicWalletID,
            agreement.ContractID,
            file,
            fieldDescriptor,
            "UploadEvidence api test successful png file")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `UploadEvidence api test failed agreement does not exist`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptor.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
            "testFile.txt",
            "text/Plain")

        val response = requestSender(user.publicWalletID,
            UUID.fromString("4232ff01-9844-4a38-9290-f84c55c211d1"),
            file,
            fieldDescriptor,
            "UploadEvidence api test failed agreement does not exist")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `UploadEvidence api test failed user does not exist`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptor.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
            "testFile.txt",
            "text/Plain")

        val response = requestSender("0x4BBb50cd3d5FF41512f5e454E980EEEaeeb4e0bb",
            agreement.ContractID,
            file,
            fieldDescriptor,
            "UploadEvidence api test failed user does not exist")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `UploadEvidence api test failed user not part of agreement`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptor.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
            "testFile.txt",
            "text/Plain")

        val response = requestSender(otherUser.publicWalletID,
            agreement.ContractID,
            file,
            fieldDescriptor,
            "UploadEvidence api test failed user not part of agreement")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `UploadEvidence api test failed file has no mime type`(){
        //documentation
        val fieldDescriptor = ArrayList<FieldDescriptor>()
        fieldDescriptor.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptor.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
            "testFile.txt",
            "")

        val response = requestSender(user.publicWalletID,
            UUID.fromString("4232ff01-9844-4a38-9290-f84c55c211d1"),
            file,
            fieldDescriptor,
            "UploadEvidence api test failed file has no mime type")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
    fun generateToken(userID: String): String? {
        val signingKey = Keys.hmacShaKeyFor("ThisIsATestKeySpecificallyForTests".toByteArray())
        return Jwts.builder()
            .setSubject(userID)
            .setExpiration(Date(System.currentTimeMillis() + 1080000))
            .signWith(signingKey)
            .compact()
    }
}