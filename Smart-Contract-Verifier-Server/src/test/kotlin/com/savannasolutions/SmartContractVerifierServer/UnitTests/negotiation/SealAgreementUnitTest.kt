package com.savannasolutions.SmartContractVerifierServer.UnitTests.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.SealAgreementRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.responses.SealAgreementResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

internal class SealAgreementUnitTest {
    private val conditionsRepository : ConditionsRepository = mock()
    private val agreementsRepository : AgreementsRepository = mock()
    private val userRepository : UserRepository = mock()
    private val judgesRepository : JudgesRepository = mock()
    private val negotiationService = NegotiationService(agreementsRepository, conditionsRepository, userRepository, judgesRepository)

    private fun parameterizeSealAgreement(paymentConditionStatus: ConditionStatus,
                                            durationConditionStatus: ConditionStatus,
                                            otherConditionStatus: ConditionStatus,
                                            paymentSet : Boolean,
                                            durationSet : Boolean,
                                            agreementExists : Boolean): SealAgreementResponse
    {
        //given
        var mockAgreementA = Agreements(
            UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            CreatedDate = Date(),
            MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockPaymentCondition = Conditions(
            UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
            "title","Payment of 500",
            paymentConditionStatus,
            Date(),).apply { contract = mockAgreementA }

        mockPaymentCondition = mockPaymentCondition.apply { proposingUser = userA }

        var mockDurationCondition = Conditions(
            UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
            "title","Duration of " + Duration.ofDays(50).seconds.toString(),
            durationConditionStatus,
            Date(),).apply { contract = mockAgreementA }

        mockDurationCondition = mockDurationCondition.apply { proposingUser = userB }

        var mockRejectedCondition = Conditions(
            UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
            "title","Reject this condition",
            otherConditionStatus,
            Date(),).apply { contract = mockAgreementA }

        mockRejectedCondition = mockRejectedCondition.apply { proposingUser = userA }

        val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockRejectedCondition)

        mockAgreementA.conditions = conditionsList

        if(durationSet)
            mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID

        if(paymentSet)
            mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        //when
        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(agreementExists)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(conditionsRepository.getById(mockDurationCondition.conditionID)).thenReturn(mockDurationCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(conditionsRepository.getAllByContract(mockAgreementA)).thenReturn(conditionsList)

        //then
        return negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))
    }


    @Test
    fun `sealAgreement successful`() {
        //given

        //when
        val response = parameterizeSealAgreement(ConditionStatus.ACCEPTED,
                                                    ConditionStatus.ACCEPTED,
                                                    ConditionStatus.ACCEPTED,
                                                    paymentSet = true,
                                                    durationSet = true,
                                                    agreementExists = true)

        //then
        Assertions.assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `sealAgreement failed agreement does not exist`() {
        //given

        //when
        val response = parameterizeSealAgreement(ConditionStatus.ACCEPTED,
                                                ConditionStatus.ACCEPTED,
                                                ConditionStatus.ACCEPTED,
                                                paymentSet = true,
                                                durationSet = true,
                                                agreementExists = false)

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a condition is pending not duration or payment`() {
        //given

        //when
        val response = parameterizeSealAgreement(ConditionStatus.ACCEPTED,
            ConditionStatus.ACCEPTED,
            ConditionStatus.PENDING,
            paymentSet = true,
            durationSet = true,
            agreementExists = true)

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement duration is not set`() {
        //given

        //when
        val response = parameterizeSealAgreement(ConditionStatus.ACCEPTED,
            ConditionStatus.ACCEPTED,
            ConditionStatus.ACCEPTED,
            paymentSet = true,
            durationSet = false,
            agreementExists = true)

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement payment not set`() {
        //given

        //when
        val response = parameterizeSealAgreement(ConditionStatus.ACCEPTED,
            ConditionStatus.ACCEPTED,
            ConditionStatus.ACCEPTED,
            paymentSet = false,
            durationSet = true,
            agreementExists = true)

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a payment condition is pending`() {
        //given

        //when
        val response = parameterizeSealAgreement(ConditionStatus.PENDING,
            ConditionStatus.ACCEPTED,
            ConditionStatus.ACCEPTED,
            paymentSet = true,
            durationSet = true,
            agreementExists = true)

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a duration condition is pending`() {
        //given

        //when
        val response = parameterizeSealAgreement(ConditionStatus.ACCEPTED,
            ConditionStatus.PENDING,
            ConditionStatus.ACCEPTED,
            paymentSet = true,
            durationSet = true,
            agreementExists = true)

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a payment condition is rejected`() {
        //given

        //when
        val response = parameterizeSealAgreement(ConditionStatus.REJECTED,
            ConditionStatus.ACCEPTED,
            ConditionStatus.ACCEPTED,
            paymentSet = true,
            durationSet = true,
            agreementExists = true)

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a duration condition is rejected`() {
        //given

        //when
        val response = parameterizeSealAgreement(ConditionStatus.ACCEPTED,
            ConditionStatus.REJECTED,
            ConditionStatus.REJECTED,
            paymentSet = true,
            durationSet = true,
            agreementExists = true)

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

}