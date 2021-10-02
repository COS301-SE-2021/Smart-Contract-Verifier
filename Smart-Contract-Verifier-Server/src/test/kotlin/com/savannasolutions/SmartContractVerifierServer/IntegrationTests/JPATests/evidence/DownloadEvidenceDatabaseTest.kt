package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.evidence

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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureDataJpa
class DownloadEvidenceDatabaseTest {
    @Autowired
    lateinit var agreementsRepository: AgreementsRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var evidenceRepository: EvidenceRepository

    @Autowired
    lateinit var linkedEvidenceRepository: LinkedEvidenceRepository

    @Autowired
    lateinit var uploadedEvidenceRepository: UploadedEvidenceRepository

    @Autowired
    lateinit var judgesRepository: JudgesRepository

    lateinit var evidenceService: EvidenceService
    var evidenceConfig = EvidenceConfig("Test")

    lateinit var userA : User
    lateinit var userB : User
    lateinit var agreement : Agreements
    var evidence : Evidence? = null
    var uploadedEvidence: UploadedEvidence? = null

    @BeforeEach
    fun beforeEach()
    {
        evidenceConfig.initialise()
        evidenceService = EvidenceService(agreementsRepository = agreementsRepository,
            userRepository = userRepository,
            evidenceRepository = evidenceRepository,
            linkedEvidenceRepository = linkedEvidenceRepository,
            uploadedEvidenceRepository = uploadedEvidenceRepository,
            judgesRepository = judgesRepository,
            evidenceConfig = evidenceConfig).apply { initialise() }

        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")
        agreement = Agreements(
            UUID.fromString("5483fed3-6ec4-48d4-8633-9c8ef90ba4d8"),
            "Test title",
            "Test description",
            CreatedDate = Date()
        )

        agreement = agreement.apply { users.add(userA) }.apply { users.add(userB) }
        userRepository.save(userA)
        userRepository.save(userB)
        agreement = agreementsRepository.save(agreement)
        userA = userA.apply { agreements.add(agreement) }
        userB = userB.apply { agreements.add(agreement) }
        userA = userRepository.save(userA)
        userB = userRepository.save(userB)

    }

    @AfterEach
    fun afterEach(){
        evidenceRepository.delete(evidence!!)
        userA.agreements.remove(agreement)
        userB.agreements.remove(agreement)
        userA = userRepository.save(userA)
        userB = userRepository.save(userB)
        agreementsRepository.delete(agreement)
        userA.conditions = emptyList()
        userB.conditions = emptyList()
        userA = userRepository.save(userA)
        userB = userRepository.save(userB)
        userRepository.delete(userA)
        userRepository.delete(userB)
    }

    private fun evidenceSaver(){
        evidence = Evidence(UUID.fromString("523ce05d-aea1-42b4-a405-2ed345e8ecb6"),
                            "A valid hash",
                            EvidenceType.UPLOADED)
        evidence!!.user = userA
        evidence!!.contract = agreement
        evidence = evidenceRepository.save(evidence!!)
        uploadedEvidence!!.evidence = evidence!!
        uploadedEvidence = uploadedEvidenceRepository.save(uploadedEvidence!!)
        evidence!!.uploadedEvidence = uploadedEvidence
        evidence = evidenceRepository.save(evidence!!)
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
    fun `DownloadEvidence successful text file`()
    {
        //given
        val file = convertTextFileToMultipartFile("src/test/kotlin/com/savannasolutions/SmartContractVerifierServer/testFiles/",
            "testFile.txt",
            "text/plain")
        evidenceConfig.filesystem.saveFile(file,file.name)
        evidence = Evidence(UUID.fromString("523ce05d-aea1-42b4-a405-2ed345e8ecb6"),
            "A valid hash",
            EvidenceType.UPLOADED)
        uploadedEvidence = UploadedEvidence(evidence!!.evidenceId,
                                            "testFile.txt",
                                            "text/plain",
                                            "testFile.txt")
        evidenceSaver()

        //when
        val response = evidenceService.downloadEvidence(userA.publicWalletID, agreement.ContractID, evidence!!.evidenceId.toString())

        //then
        assertNotNull(response.file)
        assertTrue(response.file!!.isFile)
    }

}