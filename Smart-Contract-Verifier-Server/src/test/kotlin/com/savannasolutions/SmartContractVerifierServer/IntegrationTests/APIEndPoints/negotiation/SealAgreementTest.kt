package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.negotiation

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
class SealAgreementTest {
    @Autowired
    lateinit var mockMvc : MockMvc

    @MockBean
    lateinit var agreementsRepository : AgreementsRepository

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var conditionsRepository: ConditionsRepository

    private lateinit var userA : User
    private lateinit var userB : User
    private lateinit var paymentConditionID: UUID
    private lateinit var durationConditionID: UUID
    private lateinit var pendingConditionID: UUID
    private lateinit var agreementUUID : UUID
    private lateinit var agreementFaultyUUID: UUID

    @BeforeEach
    fun beforeEach()
    {

        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")

        val agreement = Agreements(
            UUID.fromString("3c5657d6-e302-48d3-b9df-dcfccec97503"),
            "test agreement",
            "test description",
            CreatedDate = Date(),
            MovedToBlockChain = false)
        agreementUUID = agreement.ContractID

        val agreementFaulty = Agreements(
            UUID.fromString("3D5657d6-e302-48d3-b9df-dcfccec97503"),
            "test agreement",
            "test description",
            CreatedDate = Date(),
            MovedToBlockChain = false)
        agreementFaultyUUID = agreementFaulty.ContractID

        val pendingCondition = Conditions(
            UUID.fromString("967ee13c-dd5d-4de5-adb5-7dd4907fb2cf"),
            "Test conditions pending",
            "Test condition description",
            ConditionStatus.PENDING,
            Date()
        )
        pendingConditionID = pendingCondition.conditionID
        pendingCondition.proposingUser = userB
        pendingCondition.contract = agreement

        val paymentCondition = Conditions(
            UUID.fromString("d20088ad-6e94-426f-84f8-bf01b9e9bf6e"),
            "Test condition accepted",
            "Test condition description",
            ConditionStatus.ACCEPTED,
            Date()
        )
        paymentConditionID = paymentCondition.conditionID
        paymentCondition.proposingUser = userA
        paymentCondition.contract = agreement

        val durationCondition = Conditions(
            UUID.fromString("e2526862-6282-4fc9-9ea7-f6dbc6064c48"),
            "Test duration of 5 days",
            "Test condition description",
            ConditionStatus.ACCEPTED,
            Date()
        )
        durationConditionID = durationCondition.conditionID
        durationCondition.proposingUser = userA
        durationCondition.contract = agreement

        agreement.DurationConditionUUID = durationConditionID
        agreement.PaymentConditionUUID = paymentConditionID

        val conditionListCorrect = ArrayList<Conditions>()
        conditionListCorrect.add(durationCondition)
        conditionListCorrect.add(paymentCondition)

        val conditionListFailure = ArrayList<Conditions>()
        conditionListFailure.add(pendingCondition)

        val userList = ArrayList<User>()
        userList.add(userA)
        userList.add(userB)

        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(agreementsRepository.getById(agreementFaulty.ContractID)).thenReturn(agreementFaulty)
        whenever(agreementsRepository.existsById(agreementFaulty.ContractID)).thenReturn(true)
        whenever(conditionsRepository.getAllByContract(agreement)).thenReturn(conditionListCorrect)
        whenever(conditionsRepository.getAllByContract(agreementFaulty)).thenReturn(conditionListFailure)
        whenever(agreementsRepository.existsById(agreementFaulty.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(agreementFaulty.ContractID)).thenReturn(agreementFaulty)
        whenever(agreementsRepository.save(agreement)).thenReturn(agreement)
        whenever(agreementsRepository.save(agreementFaulty)).thenReturn(agreementFaulty)
        for(condition in conditionListCorrect)
        {
            whenever(conditionsRepository.existsById(condition.conditionID)).thenReturn(true)
            whenever(conditionsRepository.getById(condition.conditionID)).thenReturn(condition)
        }
        for(condition in conditionListFailure)
        {
            whenever(conditionsRepository.existsById(condition.conditionID)).thenReturn(true)
            whenever(conditionsRepository.getById(condition.conditionID)).thenReturn(condition)
        }
    }

    private fun requestSender(rjson: String) : MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/negotiation/seal-agreement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)).andReturn().response
    }

    @Test
    fun `SealAgreement successful`()
    {
        val rjson = "{\"AgreementID\" : \"${agreementUUID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `SealAgreement failure conditions not accepted or rejected`()
    {
        val rjson = "{\"AgreementID\" : \"${agreementFaultyUUID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }


}