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
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

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
    fun `sealAgreement successful`() {
        //given
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                PartyA = "b6060b01-1505-4d8d-8294-0c5495e26441",
                PartyB = "df8dc898-bd7a-4bdb-9e36-781b70784528",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                                                "Payment of 500",
                                                ConditionStatus.ACCEPTED,
                                                "b6060b01-1505-4d8d-8294-0c5495e26441",
                                                Date(),
                                                mockAgreementA)

        val mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                                                "Duration of " + Duration.ofDays(50).seconds.toString(),
                                                ConditionStatus.ACCEPTED,
                                                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                                                Date(),
                                                mockAgreementA)

        val mockRejectedCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                                                    "Reject this condition",
                                                    ConditionStatus.REJECTED,
                                                    "df8dc898-bd7a-4bdb-9e36-781b70784528",
                                                    Date(),
                                                    mockAgreementA)

        val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockRejectedCondition)

        mockAgreementA.conditions = conditionsList

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID
        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(conditionsRepository.getById(mockDurationCondition.conditionID)).thenReturn(mockDurationCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `sealAgreement failed agreement does not exist`() {
        //given

        whenever(agreementsRepository.existsById(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"))).thenReturn(false)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce")))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a condition is pending not duration or payment`() {
        //given
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                PartyA = "b6060b01-1505-4d8d-8294-0c5495e26441",
                PartyB = "df8dc898-bd7a-4bdb-9e36-781b70784528",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "Payment of 500",
                ConditionStatus.ACCEPTED,
                "b6060b01-1505-4d8d-8294-0c5495e26441",
                Date(),
                mockAgreementA)

        val mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.ACCEPTED,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val mockPendingCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "Reject this condition",
                ConditionStatus.PENDING,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockPendingCondition)

        mockAgreementA.conditions = conditionsList

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID
        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement duration is not set`() {
        //given
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                PartyA = "b6060b01-1505-4d8d-8294-0c5495e26441",
                PartyB = "df8dc898-bd7a-4bdb-9e36-781b70784528",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "Payment of 500",
                ConditionStatus.ACCEPTED,
                "b6060b01-1505-4d8d-8294-0c5495e26441",
                Date(),
                mockAgreementA)

        val mockPendingCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "Reject this condition",
                ConditionStatus.PENDING,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockPendingCondition)

        mockAgreementA.conditions = conditionsList

        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement payment not set`() {
        //given
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                PartyA = "b6060b01-1505-4d8d-8294-0c5495e26441",
                PartyB = "df8dc898-bd7a-4bdb-9e36-781b70784528",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.ACCEPTED,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val mockPendingCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "Reject this condition",
                ConditionStatus.PENDING,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPendingCondition)

        mockAgreementA.conditions = conditionsList

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a payment condition is pending`() {
        //given
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                PartyA = "b6060b01-1505-4d8d-8294-0c5495e26441",
                PartyB = "df8dc898-bd7a-4bdb-9e36-781b70784528",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "Payment of 500",
                ConditionStatus.PENDING,
                "b6060b01-1505-4d8d-8294-0c5495e26441",
                Date(),
                mockAgreementA)

        val mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.ACCEPTED,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val mockOtherCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "Reject this condition",
                ConditionStatus.REJECTED,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockOtherCondition)

        mockAgreementA.conditions = conditionsList

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID
        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a duration condition is pending`() {
        //given
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                PartyA = "b6060b01-1505-4d8d-8294-0c5495e26441",
                PartyB = "df8dc898-bd7a-4bdb-9e36-781b70784528",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "Payment of 500",
                ConditionStatus.ACCEPTED,
                "b6060b01-1505-4d8d-8294-0c5495e26441",
                Date(),
                mockAgreementA)

        val mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.PENDING,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val mockOtherCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "Reject this condition",
                ConditionStatus.ACCEPTED,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockOtherCondition)

        mockAgreementA.conditions = conditionsList

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID
        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a payment condition is rejected`() {
        //given
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                PartyA = "b6060b01-1505-4d8d-8294-0c5495e26441",
                PartyB = "df8dc898-bd7a-4bdb-9e36-781b70784528",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "Payment of 500",
                ConditionStatus.REJECTED,
                "b6060b01-1505-4d8d-8294-0c5495e26441",
                Date(),
                mockAgreementA)

        val mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.ACCEPTED,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val mockOtherCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "Reject this condition",
                ConditionStatus.ACCEPTED,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockOtherCondition)

        mockAgreementA.conditions = conditionsList

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID
        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(conditionsRepository.getById(mockDurationCondition.conditionID)).thenReturn(mockDurationCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a duration condition is rejected`() {
        //given
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                PartyA = "b6060b01-1505-4d8d-8294-0c5495e26441",
                PartyB = "df8dc898-bd7a-4bdb-9e36-781b70784528",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "Payment of 500",
                ConditionStatus.ACCEPTED,
                "b6060b01-1505-4d8d-8294-0c5495e26441",
                Date(),
                mockAgreementA)

        val mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.REJECTED,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val mockOtherCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "Reject this condition",
                ConditionStatus.ACCEPTED,
                "df8dc898-bd7a-4bdb-9e36-781b70784528",
                Date(),
                mockAgreementA)

        val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockOtherCondition)

        mockAgreementA.conditions = conditionsList

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID
        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(conditionsRepository.getById(mockDurationCondition.conditionID)).thenReturn(mockDurationCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getConditionDetails successful`(){
        //given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")
        val mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),PartyA = "User A",
                PartyB = "User B",
                CreatedDate = Date(),
                MovedToBlockChain = false)
        val mockConditionA = Conditions(conditionAUUID,"",ConditionStatus.ACCEPTED,
                "UserA",Date(), mockAgreementA)

        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)
        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)

        //when
        val response = negotiationService.getConditionDetails(GetConditionDetailsRequest(conditionAUUID))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getConditionDetails condition does not exist`(){
        //given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")

        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(false)

        //when
        val response = negotiationService.getConditionDetails(GetConditionDetailsRequest(conditionAUUID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setPaymentCondition successful`(){
        //given
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Payment of 500.0",
                ConditionStatus.PENDING,
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.setPaymentCondition(SetPaymentConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                500.0))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `setPayment Agreement does not exist`(){
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

        whenever(agreementsRepository.existsById(mockCondition.contract.ContractID)).thenReturn(false)

        //when
        val response = negotiationService.setPaymentCondition(SetPaymentConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                500.0))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setPayment Payment is a negative value`(){
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Payment of -500.0",
                ConditionStatus.PENDING,
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.setPaymentCondition(SetPaymentConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                -500.0))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setPayment condition proposed user is empty`(){
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
        val response = negotiationService.setPaymentCondition(SetPaymentConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                500.0))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setPayment proposed user is not part of agreement`(){
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Payment of 500.0",
                ConditionStatus.PENDING,
                "Not valid user",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.setPaymentCondition(SetPaymentConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                500.0))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setDurationCondition successful`(){
        //given
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Duration of " + Duration.ofSeconds(500).seconds,
                ConditionStatus.PENDING,
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.setDurationCondition(SetDurationConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                Duration.ofSeconds(500)))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `setDuration Agreement does not exist`(){
        //given
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Duration of " + Duration.ofSeconds(500).seconds,
                ConditionStatus.PENDING,
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockCondition.contract.ContractID)).thenReturn(false)

        //when
        val response = negotiationService.setDurationCondition(SetDurationConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                Duration.ofSeconds(500)))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setDuration Duration is a negative value`(){
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Duration of " + Duration.ofSeconds(-500).seconds,
                ConditionStatus.PENDING,
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.setDurationCondition(SetDurationConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                Duration.ofSeconds(-500)))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setDuration condition proposed user is empty`(){
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Duration of " + Duration.ofSeconds(500).seconds,
                ConditionStatus.PENDING,
                "",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.setDurationCondition(SetDurationConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                Duration.ofSeconds(500)))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setDuration proposed user is not part of agreement`(){
        val mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                PartyA = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                PartyB = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "Duration of " + Duration.ofSeconds(500).seconds,
                ConditionStatus.PENDING,
                "Not valid user",
                Date(),
                mockAgreement)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)

        //when
        val response = negotiationService.setDurationCondition(SetDurationConditionRequest(mockCondition.proposingUser,
                mockCondition.contract.ContractID,
                Duration.ofSeconds(500)))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }
}