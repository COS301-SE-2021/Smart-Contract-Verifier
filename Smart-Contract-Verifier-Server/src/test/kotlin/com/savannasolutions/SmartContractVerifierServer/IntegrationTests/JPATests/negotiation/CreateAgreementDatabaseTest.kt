package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.CreateAgreementRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureDataJpa
class CreateAgreementDatabaseTest {
    @Autowired
    lateinit var agreementsRepository: AgreementsRepository

    @Autowired
    lateinit var conditionsRepository: ConditionsRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var judgesRepository: JudgesRepository

    private lateinit var userA : User
    private lateinit var userB : User
    private lateinit var agreement : Agreements

    private lateinit var negotiationService: NegotiationService

    @BeforeEach
    fun beforeEach()
    {
        negotiationService = NegotiationService(agreementsRepository,
                                                conditionsRepository,
                                                userRepository,
                                                judgesRepository)
        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")
        userRepository.save(userA)
        userRepository.save(userB)
    }

    @AfterEach
    fun afterEach()
    {
        userRepository.delete(userA)
        userRepository.delete(userB)
        agreementsRepository.delete(agreement)
    }

    @Test
    fun `CreateAgreement successful`()
    {
        val request = CreateAgreementRequest(userB.publicWalletID,
                                                "Integration test title",
                                                "This tests the agreement save functionality",
                                                "www.dodgy_url.com")

        val response = negotiationService.createAgreement(userA.publicWalletID, request)
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.responseObject)
        assertNotNull(response.responseObject!!.agreementID)
        agreement = agreementsRepository.getById(response.responseObject!!.agreementID!!)
        assertEquals(agreement.AgreementTitle, request.Title)
        assertEquals(agreement.AgreementDescription, request.Description)
        assertEquals(agreement.AgreementImageURL, request.ImageURL)
        val users = userRepository.getUsersByAgreementsContaining(agreement)
        assertContains(users, userA)
        assertContains(users, userB)
    }
}