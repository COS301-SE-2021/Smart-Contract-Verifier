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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import java.util.*

//@SpringBootTest
internal class NegotiationServiceTest//@Autowired
() {
    private lateinit var conditionsRepository: ConditionsRepository
    private lateinit var agreementsRepository: AgreementsRepository
    private lateinit var negotiationService : NegotiationService

    @BeforeEach
    fun setUp() {
        conditionsRepository = Mockito.mock(ConditionsRepository::class.java)
        agreementsRepository = Mockito.mock(AgreementsRepository::class.java)
        negotiationService = NegotiationService(agreementsRepository, conditionsRepository)
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
                                        null, null);

        val conditionARequest = AcceptConditionRequest(conditionAUUID)
        var mockConditionA = Conditions(conditionAUUID,"",ConditionStatus.PENDING,
                                        "UserA",Date(), mockAgreementA)

        Mockito.`when`(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)
        Mockito.`when`(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)

        //When
        val response = negotiationService.acceptCondition(conditionARequest)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
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