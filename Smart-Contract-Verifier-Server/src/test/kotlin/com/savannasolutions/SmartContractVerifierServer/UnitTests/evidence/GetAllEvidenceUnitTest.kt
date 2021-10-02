package com.savannasolutions.SmartContractVerifierServer.UnitTests.evidence

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.LinkedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.UploadedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.responses.GetAllEvidenceResponse
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
import kotlin.io.path.Path
import kotlin.test.*

internal class GetAllEvidenceUnitTest {
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
    private var linkedEvidence: Evidence? = null
    private var uploadedFile: MultipartFile? = null

    @AfterEach
    fun afterEach()
    {
        evidenceConfig.filesystem.deleteFile(uploadedFile!!.originalFilename!!)
    }

    private fun parameterizedGetAllEvidence(userExists: Boolean,
                                            agreementExists: Boolean,
                                            userPartOfAgreement: Boolean,
                                            fileName: String,
                                            fileMimeType: String,
                                            fileURL: String): ApiResponse<GetAllEvidenceResponse>
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
        uploadedEvidence = Evidence(
            UUID.fromString("7d793c67-10e8-419b-8137-be9758594184"),
            "aUseFulHash",
            EvidenceType.UPLOADED)
        uploadedEvidence!!.user = user
        uploadedEvidence!!.contract = agreement
        uploadedEvidence!!.uploadedEvidence = uploadedEvidenceObj
        uploadedEvidenceObj.evidence = uploadedEvidence!!

        linkedEvidence = Evidence(UUID.fromString("7d793c67-10e8-419b-8137-be9758594184"),"linked evidence", EvidenceType.LINKED)
        linkedEvidence!!.user = user
        linkedEvidence!!.contract = agreement

        val linkedEvidenceObj = LinkedEvidence(UUID.fromString("523ce05d-aea1-42b4-a405-2ed345e8ecb6"),
                                            fileURL)
        linkedEvidenceObj.evidence = linkedEvidence!!
        linkedEvidence!!.evidenceUrl = linkedEvidenceObj

        val evidenceList = ArrayList<Evidence>()
        evidenceList.add(linkedEvidence!!)
        evidenceList.add(uploadedEvidence!!)


        uploadedFile = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/", fileName, fileMimeType)
        //when
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(agreementExists)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(userExists)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(evidenceRepository.existsById(uploadedEvidence!!.evidenceId)).thenReturn(true)
        whenever(evidenceRepository.getById(uploadedEvidence!!.evidenceId)).thenReturn(uploadedEvidence)
        whenever(evidenceRepository.existsById(linkedEvidence!!.evidenceId)).thenReturn(true)
        whenever(evidenceRepository.getById(linkedEvidence!!.evidenceId)).thenReturn(linkedEvidence)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(agreement.users.toList())
        whenever(evidenceRepository.getAllByContract(agreement)).thenReturn(evidenceList)
        evidenceConfig.filesystem.saveFile(uploadedFile!!, fileName)

        //then
        return if(userPartOfAgreement){
            evidenceService.getAllEvidence(user.publicWalletID, agreement.ContractID)
        } else
            evidenceService.getAllEvidence(otherUser.publicWalletID, agreement.ContractID)

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
    fun `GetAllEvidence successful`(){
        //given

        //when
        val response = parameterizedGetAllEvidence(userExists = true,
                                                    agreementExists = true,
                                                    userPartOfAgreement = true,
                                                    "testFile.txt",
                                                    "text/plain",
                                                    "https://dodgy.url")

        //then
        assertNotNull(response.responseObject)
        assertNotNull(response.responseObject!!.linkedEvidenceDetails)
        assertFalse(response.responseObject!!.linkedEvidenceDetails!!.isEmpty())
        assertNotNull(response.responseObject!!.uploadedEvidenceDetails)
        assertFalse(response.responseObject!!.uploadedEvidenceDetails!!.isEmpty())
        assertEquals(response.responseObject!!.linkedEvidenceDetails!![0].evidenceID, linkedEvidence!!.evidenceId)
        assertEquals(response.responseObject!!.uploadedEvidenceDetails!![0].evidenceID, uploadedEvidence!!.evidenceId)
    }

    @Test
    fun `GetAllEvidence failed user does not exist`(){
        //given

        //when
        val response = parameterizedGetAllEvidence(userExists = false,
            agreementExists = true,
            userPartOfAgreement = true,
            "testFile.txt",
            "text/plain",
            "https://dodgy.url")

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `GetAllEvidence failed agreement does not exist`(){
        //given

        //when
        val response = parameterizedGetAllEvidence(userExists = true,
            agreementExists = false,
            userPartOfAgreement = true,
            "testFile.txt",
            "text/plain",
            "https://dodgy.url")

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `GetAllEvidence failed user is not part of the agreement`(){
        //given

        //when
        val response = parameterizedGetAllEvidence(userExists = true,
            agreementExists = true,
            userPartOfAgreement = false,
            "testFile.txt",
            "text/plain",
            "https://dodgy.url")

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

}