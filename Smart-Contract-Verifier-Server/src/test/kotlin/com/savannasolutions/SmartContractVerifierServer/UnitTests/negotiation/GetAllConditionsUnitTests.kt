package com.savannasolutions.SmartContractVerifierServer.UnitTests.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.responses.GetAllConditionsResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertEquals

internal class GetAllConditionsUnitTests {
    private val conditionsRepository : ConditionsRepository = mock()
    private val agreementsRepository : AgreementsRepository = mock()
    private val userRepository : UserRepository = mock()
    private val judgesRepository : JudgesRepository = mock()
    private val negotiationService = NegotiationService(agreementsRepository, conditionsRepository, userRepository, judgesRepository)

    private val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
    private val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")
    private val otherUser = User("other user")
    private lateinit var mockAgreementA : Agreements
    private val agreementID = UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8")

    private fun parameterizedGetAllConditions(userID: String,
                                                agreementID: UUID,
                                                agreementExists: Boolean): GetAllConditionsResponse
    {
        //given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")
        mockAgreementA = Agreements(
            agreementID,
            CreatedDate = Date(),
            MovedToBlockChain = false)

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        val mockConditionA = Conditions(conditionAUUID,"title","", ConditionStatus.ACCEPTED,
            Date(),).apply { contract = mockAgreementA }

        mockConditionA.apply { proposingUser = userA }

        //when
        whenever(agreementsRepository.existsById(agreementID)).thenReturn(agreementExists)
        whenever(agreementsRepository.getById(agreementID)).thenReturn(mockAgreementA)
        whenever(userRepository.getUsersByAgreementsContaining(mockAgreementA)).thenReturn(mockAgreementA.users.toList())
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)

        //then
        return negotiationService.getAllConditions(userID, agreementID)
    }

    @Test
    fun `getAllConditions successful with conditions`() {
        //given

        //when
        val response = parameterizedGetAllConditions(userA.publicWalletID,
                                                        agreementID,
                                                            true)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getAllConditions successful without conditions`() {
        //given
        var mockAgreementB = Agreements(
            UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            CreatedDate = Date(),
            MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementB = mockAgreementB.apply { users.add(userA) }
        mockAgreementB = mockAgreementB.apply { users.add(userB) }

        whenever(agreementsRepository.existsById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(true)
        whenever(agreementsRepository.getById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(mockAgreementB)
        whenever(userRepository.getUsersByAgreementsContaining(mockAgreementB)).thenReturn(mockAgreementB.users.toList())
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.getAllConditions(userA.publicWalletID,
            UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getAllConditions agreement does not exists`() {
        //given

        //when
        val response = parameterizedGetAllConditions(userA.publicWalletID,
                                                UUID.fromString("39c965fb-0277-49d9-912d-da1a6227725d"),
                                                false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAllConditions user is not part of agreement`(){
        //given

        //when
        val response = parameterizedGetAllConditions(otherUser.publicWalletID,
            UUID.fromString("39c965fb-0277-49d9-912d-da1a6227725d"), true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }
}