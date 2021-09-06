package com.savannasolutions.SmartContractVerifierServer.UnitTests.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.responses.GetAgreementDetailsResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals

internal class GetAgreementDetailsUnitTest {
    private val conditionsRepository : ConditionsRepository = mock()
    private val agreementsRepository : AgreementsRepository = mock()
    private val userRepository : UserRepository = mock()
    private val judgesRepository : JudgesRepository = mock()
    private val negotiationService = NegotiationService(agreementsRepository, conditionsRepository, userRepository, judgesRepository)

    private val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
    private val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")
    private val otherUser = User("other")

    private fun parameterizedGetAgreementDetails(userID: String,
                                                agreementExists: Boolean): ApiResponse<GetAgreementDetailsResponse>
    {
        //given
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
            CreatedDate = Date(),
            MovedToBlockChain = false)

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        val reqUser = User(userID)

        val mockCondition = Conditions(
            UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Unit test",
            ConditionStatus.PENDING,
            Date(),).apply { contract = mockAgreement }

        mockCondition.apply { proposingUser = reqUser }

        val agreementUser = ArrayList<User>()
        agreementUser.add(userA)
        agreementUser.add(userB)

        //when
        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(agreementExists)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.getUsersByAgreementsContaining(mockAgreement)).thenReturn(agreementUser)

        //then
        return negotiationService.getAgreementDetails(userID, mockAgreement.ContractID)
    }

    @Test
    fun `getAgreementDetails successful with condition`() {
        //given

        //when
        val response = parameterizedGetAgreementDetails(userA.publicWalletID,true)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getAgreementDetails successful without condition`() {
        //given
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
            CreatedDate = Date(),
            MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        val agreementUser = ArrayList<User>()
        agreementUser.add(userA)
        agreementUser.add(userB)

        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(userRepository.getUsersByAgreementsContaining(mockAgreement)).thenReturn(agreementUser)

        //when
        val response = negotiationService.getAgreementDetails(userA.publicWalletID, mockAgreement.ContractID)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getAgreementDetails agreement does not exist`() {
        //given

        //when
        val response = parameterizedGetAgreementDetails(userA.publicWalletID, false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAgreementDetails user does not exist`()
    {
        //given

        //when
        val response = parameterizedGetAgreementDetails("other user", false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAgreementDetails user is not part of the agreement`()
    {
        //given

        //when
        val response = parameterizedGetAgreementDetails(otherUser.publicWalletID, true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }



}