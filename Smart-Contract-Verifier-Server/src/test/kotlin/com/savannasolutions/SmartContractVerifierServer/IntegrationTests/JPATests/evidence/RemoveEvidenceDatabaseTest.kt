package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.evidence

import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.LinkedEvidence
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
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureDataJpa
class RemoveEvidenceDatabaseTest {
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
    lateinit var linkedEvidence: LinkedEvidence
    lateinit var lEvidence: Evidence
    lateinit var uploadedEvidence: UploadedEvidence
    lateinit var uEvidence: Evidence

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
        //linked
        linkedEvidence = LinkedEvidence(UUID.fromString("c6671a40-6002-4338-ad2d-a26b51507949"),
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        lEvidence = Evidence(UUID.fromString("c6671a40-6002-4338-ad2d-a26b51507949"),
            "linkedEvidenceHash",
            EvidenceType.LINKED)
        lEvidence.user = userA
        lEvidence = evidenceRepository.save(lEvidence)
        linkedEvidence.evidence = lEvidence
        linkedEvidence = linkedEvidenceRepository.save(linkedEvidence)
        lEvidence.evidenceUrl = linkedEvidence
        lEvidence = evidenceRepository.save(lEvidence)

        //uploaded
        uploadedEvidence = UploadedEvidence(UUID.fromString("c6671a40-6002-4338-ad2d-a26b51507949"),
            "testFile.txt",
            "text/plain",
            "testFile.txt",)
        uEvidence = Evidence(UUID.fromString("c6671a40-6002-4338-ad2d-a26b51507949"),
            "linkedEvidenceHash",
            EvidenceType.UPLOADED)
        uEvidence.user = userA
        uEvidence = evidenceRepository.save(uEvidence)
        uploadedEvidence.evidence = uEvidence
        uploadedEvidence = uploadedEvidenceRepository.save(uploadedEvidence)
        uEvidence.uploadedEvidence = uploadedEvidence
        uEvidence = evidenceRepository.save(uEvidence)
    }

    @AfterEach
    fun afterEach(){
        evidenceRepository.delete(lEvidence)
        evidenceRepository.delete(uEvidence)
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

    @Test
    fun `Remove file successful database test`(){
        //given

        //when
        evidenceService.removeEvidence(userA.publicWalletID, agreement.ContractID, lEvidence.evidenceId.toString())
        evidenceService.removeEvidence(userA.publicWalletID, agreement.ContractID, uEvidence.evidenceId.toString())

        //then
        val testLEvidence = evidenceRepository.getById(lEvidence.evidenceId)
        val testUEvidence = evidenceRepository.getById(uEvidence.evidenceId)
        assertTrue(testLEvidence.removed)
        assertTrue(testUEvidence.removed)
    }
}