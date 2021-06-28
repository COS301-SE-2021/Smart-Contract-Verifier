package com.savannasolutions.SmartContractVerifierServer.services

import com.savannasolutions.SmartContractVerifierServer.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.requests.*
import com.savannasolutions.SmartContractVerifierServer.responses.ResponseStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

internal class NegotiationServiceTest
{
    private val conditionsRepository : ConditionsRepository = mock()
    private val agreementsRepository : AgreementsRepository = mock()
    private val negotiationService = NegotiationService(agreementsRepository, conditionsRepository)

    @Test
    fun `acceptCondition successful accept`() {
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
    fun `acceptCondition condition does not exist`()
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
    fun `acceptCondition condition is already rejected`(){
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
    fun `acceptCondition condition is already accepted`()
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
    fun `createAgreement successful`() {
        //given
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                                        PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                                        PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                                        CreatedDate = Date(),
                                        MovedToBlockChain = false)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreement)

        //when
        val response = negotiationService.createAgreement(CreateAgreementRequest("0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                                                                                 "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4"))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `createAgreement Party A is empty`(){
        //given

        //when
        val response = negotiationService.createAgreement(CreateAgreementRequest("",
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4"))

        //then
        assertEquals(ResponseStatus.FAILED, response.status)
    }

    @Test
    fun `createAgreement Party B is empty`(){
        //given

        //when
        val response = negotiationService.createAgreement(CreateAgreementRequest(
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", ""))

        //then
        assertEquals(ResponseStatus.FAILED, response.status)
    }

    @Test
    fun `createAgreement Party A and B are the same`(){
        //given

        //when
        val response = negotiationService.createAgreement(CreateAgreementRequest(
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4"))

        //then
        assertEquals(ResponseStatus.FAILED, response.status)
    }

    @Test
    fun `createCondition successful`() {
        //given
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                        "Unit test",
                                        ConditionStatus.PENDING,
                                        "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                                            Date(),
                                        mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.createCondition(CreateConditionRequest(mockCondition.proposingUser,
                                                            mockCondition.contract.ContractID,
                                                            mockCondition.conditionDescription))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `createCondition Agreement does not exist`(){
        //given
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Unit test",
                ConditionStatus.PENDING,
                "USER A",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockCondition.contract.ContractID)).thenReturn(false)

        //when
        val response = negotiationService.createCondition(CreateConditionRequest(mockCondition.proposingUser,
                                                                                    mockCondition.contract.ContractID,
                                                                                    mockCondition.conditionDescription))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `createCondition condition description is empty`(){
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "",
                ConditionStatus.PENDING,
                "USER A",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.createCondition(CreateConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                mockCondition.conditionDescription))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `createCondition condition proposed user is empty`(){
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Unit test",
                ConditionStatus.PENDING,
                "",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.createCondition(CreateConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                mockCondition.conditionDescription))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `createCondition condition proposed user is not part of agreement`(){
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Unit test",
                ConditionStatus.PENDING,
                "Not valid user",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.createCondition(CreateConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                mockCondition.conditionDescription))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAgreementDetails successful with condition`() {
        //given
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Unit test",
                ConditionStatus.PENDING,
                "Not valid user",
                Date(),
                mockAgreement)
        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)

        //when
        val response = negotiationService.getAgreementDetails(GetAgreementDetailsRequest(mockAgreement.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getAgreementDetails successful without condition`() {
        //given
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)

        //when
        val response = negotiationService.getAgreementDetails(GetAgreementDetailsRequest(mockAgreement.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getAgreementDetails agreement does not exist`() {
        //given
        val posUUID = "7fa870d3-2119-4b41-8062-46e2d5136937"
        whenever(agreementsRepository.existsById(UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"))).thenReturn(false)

        //when
        val response = negotiationService.getAgreementDetails(GetAgreementDetailsRequest(UUID.fromString(posUUID)))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `rejectCondition successful reject`() {
        //Given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")
        val mockAgreementA = Agreements(UUID.randomUUID(),PartyA = "User A",
                                        PartyB = "User B",
                                        CreatedDate = Date(),
                                        MovedToBlockChain = false)

        val mockConditionA = Conditions(conditionAUUID,"",ConditionStatus.PENDING,
                "UserA",Date(), mockAgreementA)

        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)
        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)

        //When
        val response = negotiationService.rejectCondition(RejectConditionRequest(conditionAUUID))

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `rejectCondition condition does not exist`()
    {
        //Given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")
        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(false)

        //When
        val response = negotiationService.rejectCondition(RejectConditionRequest(conditionAUUID))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `rejectCondition condition is already rejected`(){
        //Given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")
        val mockAgreementA = Agreements(UUID.randomUUID(),PartyA = "User A",
                PartyB = "User B",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockConditionA = Conditions(conditionAUUID,"",ConditionStatus.REJECTED,
                "UserA",Date(), mockAgreementA)

        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)
        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)

        //when
        val response = negotiationService.rejectCondition(RejectConditionRequest(conditionAUUID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)

    }

    @Test
    fun `rejectCondition condition is already accepted`()
    {
        //Given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")
        val mockAgreementA = Agreements(UUID.randomUUID(),PartyA = "User A",
                PartyB = "User B",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val conditionARequest = RejectConditionRequest(conditionAUUID)
        val mockConditionA = Conditions(conditionAUUID,"",ConditionStatus.ACCEPTED,
                "UserA",Date(), mockAgreementA)

        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)
        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)

        //when
        val response = negotiationService.rejectCondition(conditionARequest)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAllConditions successful with conditions`() {
        //given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),PartyA = "User A",
                PartyB = "User B",
                CreatedDate = Date(),
                MovedToBlockChain = false)
        val mockConditionA = Conditions(conditionAUUID,"",ConditionStatus.ACCEPTED,
                "UserA",Date(), mockAgreementA)

        whenever(agreementsRepository.existsById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(true)
        whenever(agreementsRepository.getById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(mockAgreementA)

        //when
        val response = negotiationService.getAllConditions(GetAllConditionsRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getAllConditions successful without conditions`() {
        //given
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),PartyA = "User A",
                PartyB = "User B",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        whenever(agreementsRepository.existsById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(true)
        whenever(agreementsRepository.getById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(mockAgreementA)

        //when
        val response = negotiationService.getAllConditions(GetAllConditionsRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getAllConditions agreement does not exists`() {
        //given
        whenever(agreementsRepository.existsById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(false)

        //when
        val response = negotiationService.getAllConditions(GetAllConditionsRequest(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8")))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
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