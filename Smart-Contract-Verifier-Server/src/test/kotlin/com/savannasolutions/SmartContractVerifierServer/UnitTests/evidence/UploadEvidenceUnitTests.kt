package com.savannasolutions.SmartContractVerifierServer.UnitTests.evidence

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.common.responseErrorMessages.commonResponseErrorMessages
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.UploadedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.services.EvidenceService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

// TODO: 2021/09/10 Add more file tests depending on final list of acceptable files 

internal class UploadEvidenceUnitTests {
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

    private fun parameterizedUploadEvidence(userExists: Boolean,
                                    agreementExists: Boolean,
                                    userPartOfAgreement: Boolean,
                                    fileName: String,
                                    fileMimeType: String) : ApiResponse<Objects>
    {
        evidenceConfig.initialise()
        evidenceService.initialise()
        //given
        val user = User("test user")
        val otherUser = User("other")
        val agreement = Agreements(UUID.fromString("377f66e7-5060-48f8-a44b-ae0bea405a5e"),
                                    CreatedDate = Date())
        agreement.users.add(user)
        val uploadedEvidence = UploadedEvidence(UUID.fromString("1981c189-afb4-431a-9fc5-d8e2e48b7110"),
                                            "testFile",
                                            "txt")
        val evidence = Evidence("aUseFulHash",
                                EvidenceType.UPLOADED)
        evidence.user = user
        evidence.contract = agreement
        evidence.uploadedEvidence = uploadedEvidence
        uploadedEvidence.evidence = evidence
        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/UnitTests/evidence/testFiles/", fileName, fileMimeType)
        //when
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(agreementExists)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(userExists)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(evidenceRepository.save(any<Evidence>())).thenReturn(evidence)
        //whenever(evidenceConfig.filesystem.saveFile(file, "testFile"))

        //then
        return if(userPartOfAgreement){
            evidenceService.uploadEvidence(user.publicWalletID, agreement.ContractID, file)
        } else
            evidenceService.uploadEvidence(otherUser.publicWalletID, agreement.ContractID, file)
    }

    private fun convertTextFileToMultipartFile(filePath:String, fileName: String, mimeType: String): MultipartFile{
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
    fun `UploadEvidence successful for text file`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
                                        agreementExists = true,
                                        userPartOfAgreement = true,
                                        "testFile.txt",
                                        "text/Plain")

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `UploadEvidence successful for pdf file`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
            agreementExists = true,
            userPartOfAgreement = true,
            "testFile.pdf",
            "application/pdf")

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `UploadEvidence successful for png file`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
            agreementExists = true,
            userPartOfAgreement = true,
            "testFile.png",
            "image/png")

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `UploadEvidence failed agreement does not exist`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
                                                    agreementExists = false,
                                                    userPartOfAgreement = true,
                                                    "testFile.txt",
                                                    "text/Plain")

        assertEquals(response.status, ResponseStatus.FAILED)
        assertNotNull(response.message)
        assertEquals(response.message!!, commonResponseErrorMessages.agreementDoesNotExist)
    }

    @Test
    fun `UploadEvidence failed user does not exist`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = false,
                                                    agreementExists = true,
                                                    userPartOfAgreement = true,
                                                    "testFile.txt",
                                                    "text/Plain")

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
        assertEquals(response.message, commonResponseErrorMessages.userDoesNotExist)
    }

    @Test
    fun `UploadEvidence failed user not part of agreement`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
            agreementExists = true,
            userPartOfAgreement = false,
            "testFile.txt",
            "text/Plain")

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
        assertEquals(response.message, commonResponseErrorMessages.userNotPartOfAgreement)
    }

    @Test
    fun `UploadEvidence failed unknown mime type`(){
        //given

        //when
        val response = parameterizedUploadEvidence(userExists = true,
            agreementExists = true,
            userPartOfAgreement = false,
            "testFile.txt",
            "")

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }
}