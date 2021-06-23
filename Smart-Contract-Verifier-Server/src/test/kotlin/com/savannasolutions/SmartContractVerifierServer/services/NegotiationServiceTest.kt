package com.savannasolutions.SmartContractVerifierServer.services

import com.savannasolutions.SmartContractVerifierServer.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.requests.*
import com.savannasolutions.SmartContractVerifierServer.responses.ResponseStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

//@SpringBootTest
internal class NegotiationServiceTest//@Autowired
() {
    private val conditionsRepository : ConditionsRepository = mock()
    private val agreementsRepository : AgreementsRepository = mock()
    private val negotiationService = NegotiationService(agreementsRepository, conditionsRepository)

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {

    }

    @Test
    fun `acceptCondition tests successful accept`() {
        //Given
        val conditionAUUID = UUID.randomUUID()
        val mockAgreementA = Agreements(UUID.randomUUID(),null,
                                        "User A", "User B",
                                        Date(), null, null,
                                        false, null,
                                        null, null)

        val conditionARequest = AcceptConditionRequest(conditionAUUID)
        val mockConditionA = Conditions(conditionAUUID,"",ConditionStatus.PENDING,
                                        "UserA",Date(), mockAgreementA)

        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)
        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)

        //When
        val response = negotiationService.acceptCondition(conditionARequest)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `acceptCondition test condition does not exist`()
    {
        //Given
        val conditionAUUID = UUID.randomUUID()

        val conditionARequest = AcceptConditionRequest(conditionAUUID)
        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(false)

        //When
        val response = negotiationService.acceptCondition(conditionARequest)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `acceptCondition test condition is already rejected`(){
        //Given
        val conditionAUUID = UUID.randomUUID()
        val mockAgreementA = Agreements(UUID.randomUUID(),null,
                "User A", "User B",
                Date(), null, null,
                false, null,
                null, null)

        val conditionARequest = AcceptConditionRequest(conditionAUUID)
        val mockConditionA = Conditions(conditionAUUID,"",ConditionStatus.REJECTED,
                "UserA",Date(), mockAgreementA)

        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)
        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)

        //when
        val response = negotiationService.acceptCondition(conditionARequest)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)

    }

    @Test
    fun `acceptCondition test condition is already accepted`()
    {
        //Given
        val conditionAUUID = UUID.randomUUID()
        val mockAgreementA = Agreements(UUID.randomUUID(),null,
                "User A", "User B",
                Date(), null, null,
                false, null,
                null, null)

        val conditionARequest = AcceptConditionRequest(conditionAUUID)
        val mockConditionA = Conditions(conditionAUUID,"",ConditionStatus.ACCEPTED,
                "UserA",Date(), mockAgreementA)

        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)
        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)

        //when
        val response = negotiationService.acceptCondition(conditionARequest)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun createAgreement() {

    }

    @Test
    fun createCondition() {

    }

    @Test
    fun getAgreementDetails() {

    }

    @Test
    fun rejectCondition() {

    }

    @Test
    fun getAllConditions() {

    }

    @Test
    fun sealAgreement() {

    }

    @Test
    fun getConditionDetails(){

    }

    @Test
    fun setPaymentCondition(){


    }

    @Test
    fun setDurationCondition()
    {

    }
}