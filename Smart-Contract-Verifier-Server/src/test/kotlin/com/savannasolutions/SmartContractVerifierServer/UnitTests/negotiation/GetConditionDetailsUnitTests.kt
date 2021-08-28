package com.savannasolutions.SmartContractVerifierServer.UnitTests.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.responses.GetConditionDetailsResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertEquals

internal class GetConditionDetailsUnitTests {
    private val conditionsRepository : ConditionsRepository = mock()
    private val agreementsRepository : AgreementsRepository = mock()
    private val userRepository : UserRepository = mock()
    private val judgesRepository : JudgesRepository = mock()
    private val negotiationService = NegotiationService(agreementsRepository, conditionsRepository, userRepository, judgesRepository)

    private val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
    private val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

    private fun parameterizedGetConditionDetails(userID: String,
                                                agreementExists: Boolean,
                                                conditionExists: Boolean): ApiResponse<GetConditionDetailsResponse>
    {
        //given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")
        var mockAgreementA = Agreements(
            UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            CreatedDate = Date(),
            MovedToBlockChain = false)

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockConditionA = Conditions(conditionAUUID,"title","", ConditionStatus.ACCEPTED,
            Date(),).apply { contract = mockAgreementA }

        mockConditionA = mockConditionA.apply { proposingUser = userA }

        //when
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(agreementExists)
        whenever(userRepository.getUsersByAgreementsContaining(mockAgreementA)).thenReturn(mockAgreementA.users.toList())
        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(conditionExists)
        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userB)

        //then
        return negotiationService.getConditionDetails(userID, mockAgreementA.ContractID, conditionAUUID)
    }

    @Test
    fun `getConditionDetails successful`(){
        //given

        //when
        val response = parameterizedGetConditionDetails(userA.publicWalletID,
                                                        agreementExists = true,
                                                        conditionExists = true)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getConditionDetails condition does not exist`(){
        //given

        //when
        val response = parameterizedGetConditionDetails(userA.publicWalletID,
            agreementExists = true,
            conditionExists = false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getConditionDetails agreement does not exist`()
    {
        //given

        //when
        val response = parameterizedGetConditionDetails(userA.publicWalletID,
            agreementExists = false,
            conditionExists = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }
}