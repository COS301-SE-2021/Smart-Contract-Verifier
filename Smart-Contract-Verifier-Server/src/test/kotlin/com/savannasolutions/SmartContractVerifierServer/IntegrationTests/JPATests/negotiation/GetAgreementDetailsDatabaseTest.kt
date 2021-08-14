package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.GetAgreementDetailsRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureDataJpa
class GetAgreementDetailsDatabaseTest {
    @Autowired
    lateinit var agreementsRepository: AgreementsRepository

    @Autowired
    lateinit var conditionsRepository: ConditionsRepository

    @Autowired
    lateinit var userRepository: UserRepository

    private lateinit var userA : User
    private lateinit var userB : User
    private lateinit var agreement : Agreements
    private lateinit var conditions: Conditions
    private lateinit var negotiationService: NegotiationService

    @BeforeEach
    fun beforeEach()
    {
        negotiationService = NegotiationService(agreementsRepository,
            conditionsRepository,
            userRepository)

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

        conditions = Conditions(UUID.fromString("231184cb-46f4-4052-b989-992035ec90bb"),
                                "Test condition",
                                "This is a test condition",
                                ConditionStatus.PENDING,
                                Date()).apply { contract = agreement }.apply { proposingUser = userA }
        conditions = conditionsRepository.save(conditions)
        val listOfConditions = ArrayList<Conditions>()
        listOfConditions.add(conditions)
        userA = userA.apply { conditions = listOfConditions }
        userA = userRepository.save(userA)
    }

    @AfterEach
    fun afterEach()
    {
        userA.agreements.remove(agreement)
        userB.agreements.remove(agreement)
        userA = userRepository.save(userA)
        userB = userRepository.save(userB)
        conditionsRepository.delete(conditions)
        agreementsRepository.delete(agreement)
        userA.conditions = emptyList()
        userB.conditions = emptyList()
        userA = userRepository.save(userA)
        userB = userRepository.save(userB)
        userRepository.delete(userA)
        userRepository.delete(userB)
    }

    @Test
    fun `GetAgreementDetails success`()
    {
        val request = GetAgreementDetailsRequest(agreement.ContractID)

        val response = negotiationService.getAgreementDetails(request)

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.agreementResponse)
        assertEquals(response.agreementResponse!!.agreementID, agreement.ContractID)
        assertEquals(response.agreementResponse!!.agreementTitle, agreement.AgreementTitle)
        assertEquals(response.agreementResponse!!.agreementImageURL, agreement.AgreementImageURL)
        assertNotNull(response.agreementResponse!!.partyA)
        assertNotNull(response.agreementResponse!!.partyB)
        assertEquals(response.agreementResponse!!.partyA!!.PublicWalletID, userA.publicWalletID)
        assertEquals(response.agreementResponse!!.partyB!!.PublicWalletID, userB.publicWalletID)
        assertNotNull(response.agreementResponse!!.conditions)
        for(cond in response.agreementResponse!!.conditions!!)
        {
            assertEquals(cond.conditionID, conditions.conditionID)
        }
    }
}