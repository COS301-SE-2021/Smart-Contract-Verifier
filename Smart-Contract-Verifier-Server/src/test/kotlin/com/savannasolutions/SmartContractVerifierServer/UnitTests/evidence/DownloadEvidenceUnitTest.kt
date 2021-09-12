package com.savannasolutions.SmartContractVerifierServer.UnitTests.evidence

import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.UploadedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.responses.DownloadEvidenceResponse
import com.savannasolutions.SmartContractVerifierServer.evidence.services.EvidenceService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class DownloadEvidenceUnitTest {
    private val agreementsRepository : AgreementsRepository = mock()
    private val userRepository : UserRepository = mock()
    private val evidenceRepository : EvidenceRepository = mock()
    private val linkedEvidenceRepository: LinkedEvidenceRepository = mock()
    private val uploadedEvidenceRepository: UploadedEvidenceRepository = mock()
    private val judgesRepository: JudgesRepository = mock()
    private val evidenceConfig = EvidenceConfig("TEST")


    private val evidenceService = EvidenceService(agreementsRepository = agreementsRepository,
        userRepository = userRepository,
        evidenceRepository = evidenceRepository,
        linkedEvidenceRepository = linkedEvidenceRepository,
        uploadedEvidenceRepository = uploadedEvidenceRepository,
        judgesRepository = judgesRepository,
        evidenceConfig = evidenceConfig)

    private var file: MultipartFile? = null

    @AfterEach
    fun afterEach()
    {
        evidenceConfig.filesystem.deleteFile(file!!.originalFilename!!)
    }

    private fun parameterizedUploadEvidence(userExists: Boolean,
                                            agreementExists: Boolean,
                                            userPartOfAgreement: Boolean,
                                            evidenceExist: Boolean,
                                            fileName: String,
                                            fileMimeType: String) : DownloadEvidenceResponse
    {
        evidenceConfig.initialise()
        evidenceService.initialise()
        //given
        val user = User("test user")
        val otherUser = User("other")
        val agreement = Agreements(
            UUID.fromString("377f66e7-5060-48f8-a44b-ae0bea405a5e"),
            CreatedDate = Date()
        )
        agreement.users.add(user)
        val uploadedEvidence = UploadedEvidence(
            UUID.fromString("1981c189-afb4-431a-9fc5-d8e2e48b7110"),
            fileName,
            fileMimeType,
            fileName)
        val evidence = Evidence(UUID.fromString("523ce05d-aea1-42b4-a405-2ed345e8ecb6"),
            "aUseFulHash",
            EvidenceType.UPLOADED)
        evidence.user = user
        evidence.contract = agreement
        evidence.uploadedEvidence = uploadedEvidence
        uploadedEvidence.evidence = evidence
        file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/", fileName, fileMimeType)
        //when
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(agreementExists)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(userExists)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(evidenceRepository.existsById(evidence.evidenceId)).thenReturn(evidenceExist)
        whenever(evidenceRepository.getById(evidence.evidenceId)).thenReturn(evidence)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(agreement.users.toList())
        evidenceConfig.filesystem.saveFile(file!!, fileName)

        //then
        return if(userPartOfAgreement){
            evidenceService.downloadEvidence(user.publicWalletID, agreement.ContractID, evidence.evidenceId.toString())
        } else
            evidenceService.downloadEvidence(otherUser.publicWalletID, agreement.ContractID, evidence.evidenceId.toString())
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
        return MockMultipartFile(fileName, fileName, mimeType, byteArr)
    }

    @Test
    fun `DownloadEvidence successful text file`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
                                                    agreementExists = true,
                                                    userPartOfAgreement = true,
                                                    evidenceExist = true,
                                                    "testFile.txt",
                                                    "text/Plain")
        //then
        assertNotNull(response.file)
        assertTrue(response.file!!.isFile)
    }

    @Test
    fun `DownloadEvidence successful pdf`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
            agreementExists = true,
            userPartOfAgreement = true,
            evidenceExist = true,
            "testFile.pdf",
            "application/pdf")
        //then
        assertNotNull(response.file)
        assertTrue(response.file!!.isFile)
    }

    @Test
    fun `DownloadEvidence successful png`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
            agreementExists = true,
            userPartOfAgreement = true,
            evidenceExist = true,
            "testFile.png",
            "image/png")
        //then
        assertNotNull(response.file)
        assertTrue(response.file!!.isFile)
    }

    @Test
    fun `DownloadEvidence failed due to user not existing`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = false,
            agreementExists = true,
            userPartOfAgreement = true,
            evidenceExist = true,
            "testFile.txt",
            "text/Plain")

        //then
        assertNull(response.file)
    }

    @Test
    fun `DownloadEvidence failed due to agreement not existing`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
            agreementExists = false,
            userPartOfAgreement = true,
            evidenceExist = true,
            "testFile.txt",
            "text/Plain")

        //then
        assertNull(response.file)
    }

    @Test
    fun `DownloadEvidence failed due to evidence not existing`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
            agreementExists = true,
            userPartOfAgreement = true,
            evidenceExist = false,
            "testFile.txt",
            "text/Plain")

        //then
        assertNull(response.file)
    }

    @Test
    fun `DownloadEvidence failed due to user not being part of agreement`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
            agreementExists = true,
            userPartOfAgreement = false,
            evidenceExist = true,
            "testFile.txt",
            "text/Plain")

        //then
        assertNull(response.file)
    }
}