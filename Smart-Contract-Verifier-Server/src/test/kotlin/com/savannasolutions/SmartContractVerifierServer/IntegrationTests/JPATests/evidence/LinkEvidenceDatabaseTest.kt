package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.evidence

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.LinkedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.requests.LinkEvidenceRequest
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
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureDataJpa
class LinkEvidenceDatabaseTest {
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
    var linkedEvidence: LinkedEvidence? = null
    var evidence: Evidence? = null

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

    @Test
    fun `link successful database test`(){
        //given
        val linkEvidenceRequest = LinkEvidenceRequest("https://www.youtube.com/watch?v=dQw4w9WgXcQ")

        //when
        val response = evidenceService.linkEvidence(userA.publicWalletID, agreement.ContractID, linkEvidenceRequest)

        //then
        val evidenceId = response.responseObject!!.evidenceId
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        evidence = evidenceRepository.getById(evidenceId)
        assertNotNull(evidence)
        linkedEvidence = evidence!!.evidenceUrl
        assertNotNull(linkedEvidence)
        assertEquals(linkedEvidence!!.evidenceUrl, "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
    }
}