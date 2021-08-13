package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.AcceptConditionRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.RejectConditionRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureDataJpa
class RejectConditionDatabaseTest {
    @Autowired
    lateinit var agreementsRepository: AgreementsRepository

    @Autowired
    lateinit var conditionsRepository: ConditionsRepository

    @Autowired
    lateinit var userRepository: UserRepository

    private lateinit var negotiationService: NegotiationService
    private lateinit var pendingCondition : Conditions
    private lateinit var acceptedCondition : Conditions
    private lateinit var rejectedCondition : Conditions

    @BeforeEach
    fun beforeEach()
    {
        negotiationService = NegotiationService(agreementsRepository,
            conditionsRepository,
            userRepository)
        pendingCondition = Conditions(UUID.fromString("b0cc41a5-bd56-4687-ae7f-e6f48c7ed972"),
            "Pending Condition",
            "This is a pending condition",
            ConditionStatus.PENDING,
            Date())

        pendingCondition = conditionsRepository.save(pendingCondition)

        acceptedCondition = Conditions(UUID.fromString("b0cc41a5-bd56-4687-ae7f-e6f48c7ed972"),
            "Accept Condition",
            "This is a accept condition",
            ConditionStatus.ACCEPTED,
            Date())

        acceptedCondition = conditionsRepository.save(acceptedCondition)

        rejectedCondition = Conditions(UUID.fromString("b0cc41a5-bd56-4687-ae7f-e6f48c7ed972"),
            "Rejected Condition",
            "This is a rejected condition",
            ConditionStatus.REJECTED,
            Date())

        rejectedCondition = conditionsRepository.save(rejectedCondition)
    }

    @AfterEach
    fun afterEach()
    {
        conditionsRepository.delete(acceptedCondition)
        conditionsRepository.delete(rejectedCondition)
        conditionsRepository.delete(pendingCondition)
    }

    @Test
    fun `AcceptCondition successful`()
    {
        val request = RejectConditionRequest(pendingCondition.conditionID)

        val response = negotiationService.rejectCondition(request)

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        val condition = conditionsRepository.getById(pendingCondition.conditionID)
        assertEquals(condition.conditionStatus, ConditionStatus.REJECTED)
    }

    @Test
    fun `AcceptCondition failed due to already being accepted`()
    {
        val request = RejectConditionRequest(acceptedCondition.conditionID)

        val response = negotiationService.rejectCondition(request)

        assertEquals(response.status, ResponseStatus.FAILED)
        val condition = conditionsRepository.getById(acceptedCondition.conditionID)
        assertEquals(condition.conditionStatus, ConditionStatus.ACCEPTED)
    }

    @Test
    fun `AcceptCondition failed due to already being rejected`()
    {
        val request = RejectConditionRequest(rejectedCondition.conditionID)

        val response = negotiationService.rejectCondition(request)

        assertEquals(response.status, ResponseStatus.FAILED)
        val condition = conditionsRepository.getById(rejectedCondition.conditionID)
        assertEquals(condition.conditionStatus, ConditionStatus.REJECTED)
    }


}