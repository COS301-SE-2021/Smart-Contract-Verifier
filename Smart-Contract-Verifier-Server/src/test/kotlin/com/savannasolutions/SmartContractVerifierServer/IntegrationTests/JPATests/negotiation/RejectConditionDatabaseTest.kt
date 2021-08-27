package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
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

    @Autowired
    lateinit var judgesRepository: JudgesRepository

    private lateinit var negotiationService: NegotiationService
    private lateinit var pendingCondition : Conditions
    private lateinit var acceptedCondition : Conditions
    private lateinit var rejectedCondition : Conditions

    private lateinit var agreement: Agreements
    private lateinit var pUser : User

    @BeforeEach
    fun beforeEach()
    {
        negotiationService = NegotiationService(agreementsRepository,
            conditionsRepository,
            userRepository,
            judgesRepository)

        pUser = User("test user")
        pUser = userRepository.save(pUser)
        var userB = User("other user")
        userB = userRepository.save(userB)

        agreement = Agreements(UUID.fromString("6e28cc77-d2e2-4221-abd7-7a6d0e84dbd3"),
            CreatedDate = Date(),
            MovedToBlockChain = true)

        val conditionList = ArrayList<Conditions>()

        agreement = agreement.apply { users.add(pUser) }
        agreement = agreement.apply { users.add(userB)}
        agreement = agreement.apply { conditions = conditionList }
        agreement = agreementsRepository.save(agreement)

        pendingCondition = Conditions(UUID.fromString("b0cc41a5-bd56-4687-ae7f-e6f48c7ed972"),
            "Pending Condition",
            "This is a pending condition",
            ConditionStatus.PENDING,
            Date()).apply { this.proposingUser = pUser }.apply { contract = agreement }

        pendingCondition = conditionsRepository.save(pendingCondition)

        acceptedCondition = Conditions(UUID.fromString("b0cc41a5-bd56-4687-ae7f-e6f48c7ed972"),
            "Accept Condition",
            "This is a accept condition",
            ConditionStatus.ACCEPTED,
            Date()).apply { this.proposingUser = pUser }.apply { contract = agreement }

        acceptedCondition = conditionsRepository.save(acceptedCondition)

        rejectedCondition = Conditions(UUID.fromString("b0cc41a5-bd56-4687-ae7f-e6f48c7ed972"),
            "Rejected Condition",
            "This is a rejected condition",
            ConditionStatus.REJECTED,
            Date()).apply { this.proposingUser = pUser }.apply { contract = agreement }

        rejectedCondition = conditionsRepository.save(rejectedCondition)

        conditionList.add(pendingCondition)
        conditionList.add(rejectedCondition)
        conditionList.add(acceptedCondition)
        agreement = agreement.apply { conditions = conditionList }
        agreement = agreementsRepository.save(agreement)
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
        val response = negotiationService.rejectCondition(pUser.publicWalletID,
                                                        agreement.ContractID,
                                                        pendingCondition.conditionID)

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        val condition = conditionsRepository.getById(pendingCondition.conditionID)
        assertEquals(condition.conditionStatus, ConditionStatus.REJECTED)
    }

    @Test
    fun `AcceptCondition failed due to already being accepted`()
    {
        val response = negotiationService.rejectCondition(pUser.publicWalletID,
            agreement.ContractID,
            acceptedCondition.conditionID)

        assertEquals(response.status, ResponseStatus.FAILED)
        val condition = conditionsRepository.getById(acceptedCondition.conditionID)
        assertEquals(condition.conditionStatus, ConditionStatus.ACCEPTED)
    }

    @Test
    fun `AcceptCondition failed due to already being rejected`()
    {
        val response = negotiationService.rejectCondition(pUser.publicWalletID,
            agreement.ContractID,
            rejectedCondition.conditionID)

        assertEquals(response.status, ResponseStatus.FAILED)
        val condition = conditionsRepository.getById(rejectedCondition.conditionID)
        assertEquals(condition.conditionStatus, ConditionStatus.REJECTED)
    }


}