package com.savannasolutions.SmartContractVerifierServer.UnitTests.evidence

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.util.*
import kotlin.collections.ArrayList
import kotlin.io.path.Path
import kotlin.test.assertEquals

internal class RemoveEvidenceUploadedUnitTest {
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

    private var uploadedEvidence: Evidence? = null
    private var uploadedFile: MultipartFile? = null

    @AfterEach
    fun afterEach()
    {
        evidenceConfig.filesystem.deleteFile(uploadedFile!!.originalFilename!!)
    }

    private fun parameterizedRemoveEvidenceUploadedUnitTest(userExists: Boolean,
                                            agreementExists: Boolean,
                                            userPartOfAgreement: Boolean,
                                            evidenceExist : Boolean,
                                            fileName: String,
                                            fileMimeType: String): ApiResponse<Objects>
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
        val uploadedEvidenceObj = UploadedEvidence(
            UUID.fromString("1981c189-afb4-431a-9fc5-d8e2e48b7110"),
            fileName,
            fileMimeType,
            fileName)
        uploadedEvidence = Evidence("aUseFulHash",
            EvidenceType.UPLOADED)
        uploadedEvidence!!.user = user
        uploadedEvidence!!.contract = agreement
        uploadedEvidence!!.uploadedEvidence = uploadedEvidenceObj
        uploadedEvidenceObj.evidence = uploadedEvidence!!

        val evidenceList = ArrayList<Evidence>()
        evidenceList.add(uploadedEvidence!!)


        uploadedFile = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/", fileName, fileMimeType)
        //when
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(agreementExists)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(userExists)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(evidenceRepository.existsById(uploadedEvidence!!.evidenceHash)).thenReturn(evidenceExist)
        whenever(evidenceRepository.getById(uploadedEvidence!!.evidenceHash)).thenReturn(uploadedEvidence)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(agreement.users.toList())
        whenever(evidenceRepository.getAllByContract(agreement)).thenReturn(evidenceList)
        evidenceConfig.filesystem.saveFile(uploadedFile!!, fileName)

        //then
        return if(userPartOfAgreement){
            evidenceService.removeEvidence(user.publicWalletID, agreement.ContractID, uploadedEvidence!!.evidenceHash)
        } else
            evidenceService.removeEvidence(otherUser.publicWalletID, agreement.ContractID, uploadedEvidence!!.evidenceHash)

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
    fun `RemoveEvidenceUploadedUnitTest successful`()
    {
        //given

        //when
        val response = parameterizedRemoveEvidenceUploadedUnitTest(userExists = true,
                                                                    agreementExists = true,
                                                                    userPartOfAgreement = true,
                                                                    evidenceExist = true,
                                                                    "testFile.txt",
                                                                    "text/plain")

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `RemoveEvidenceUploadedUnitTest failed user does not exist`()
    {
        //given

        //when
        val response = parameterizedRemoveEvidenceUploadedUnitTest(userExists = false,
            agreementExists = true,
            userPartOfAgreement = true,
            evidenceExist = false,
            "testFile.txt",
            "text/plain")

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RemoveEvidenceUploadedUnitTest failed agreement does not exist`()
    {
        //given

        //when
        val response = parameterizedRemoveEvidenceUploadedUnitTest(userExists = true,
            agreementExists = false,
            userPartOfAgreement = true,
            evidenceExist = true,
            "testFile.txt",
            "text/plain")

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RemoveEvidenceUploadedUnitTest failed user not part of agreement`()
    {
        //given

        //when
        val response = parameterizedRemoveEvidenceUploadedUnitTest(userExists = false,
            agreementExists = true,
            userPartOfAgreement = false,
            evidenceExist = true,
            "testFile.txt",
            "text/plain")

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RemoveEvidenceUploadedUnitTest failed evidence does not exist`()
    {
        //given

        //when
        val response = parameterizedRemoveEvidenceUploadedUnitTest(userExists = false,
            agreementExists = true,
            userPartOfAgreement = false,
            evidenceExist = false,
            "testFile.txt",
            "text/plain")

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

}