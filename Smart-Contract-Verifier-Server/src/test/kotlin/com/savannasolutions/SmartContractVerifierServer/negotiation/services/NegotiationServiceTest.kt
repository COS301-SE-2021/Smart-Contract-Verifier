package com.savannasolutions.SmartContractVerifierServer.negotiation.services

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.*
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
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
    private val userRepository : UserRepository = mock()
    private val negotiationService = NegotiationService(agreementsRepository, conditionsRepository, userRepository,)

    @Test
    fun `acceptCondition successful accept`() {
        //Given
        val conditionAUUID = UUID.randomUUID()
        var mockAgreementA = Agreements(UUID.randomUUID(),
                                        CreatedDate = Date(),
                                        MovedToBlockChain = false,)

        val userA = User("UserA","uA")
        val userB = User("UserB", "uB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        val conditionARequest = AcceptConditionRequest(conditionAUUID)
        var mockConditionA = Conditions(conditionAUUID,
                            "title",
                                        "",
                                        ConditionStatus.PENDING,
                                        Date()).apply { contract = mockAgreementA }

        mockConditionA = mockConditionA.apply { proposingUser = userA }

        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)
        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)

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
        var mockAgreementA = Agreements(UUID.randomUUID(),
                CreatedDate = Date(),
                MovedToBlockChain = false,)

        val userA = User("UserA","uA")
        val userB = User("UserB", "uB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        val conditionARequest = AcceptConditionRequest(conditionAUUID)
        var mockConditionA = Conditions(conditionAUUID,"","title",ConditionStatus.REJECTED,
                Date()).apply { contract = mockAgreementA }

        mockConditionA = mockConditionA.apply { proposingUser = userA }

        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)
        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)

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
        var mockAgreementA = Agreements(UUID.randomUUID(),
                CreatedDate = Date(),
                MovedToBlockChain = false,)

        val userA = User("UserA", "uA")
        val userB = User("UserB", "uB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        val conditionARequest = AcceptConditionRequest(conditionAUUID)
        var mockConditionA = Conditions(conditionAUUID,"","title",ConditionStatus.ACCEPTED,
                Date()).apply { contract = mockAgreementA }

        mockConditionA = mockConditionA.apply { proposingUser = userA }

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
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                                        CreatedDate = Date(),
                                        MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB)}

        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreement)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.createAgreement(CreateAgreementRequest("0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                                                                                 "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4","Mock", "Mock", "image.co.za"))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `createAgreement Party A is empty`(){
        //given

        //when
        val response = negotiationService.createAgreement(CreateAgreementRequest("",
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "Mock", "Mock", "image.co.za"))

        //then
        assertEquals(ResponseStatus.FAILED, response.status)
    }

    @Test
    fun `createAgreement Party B is empty`(){
        //given

        //when
        val response = negotiationService.createAgreement(CreateAgreementRequest(
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "", "Mock", "Mock", "image.co.za"))

        //then
        assertEquals(ResponseStatus.FAILED, response.status)
    }

    @Test
    fun `createAgreement Party A and B are the same`(){
        //given

        //when
        val response = negotiationService.createAgreement(CreateAgreementRequest(
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
                "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "Mock", "Mock", "image.co.za"))

        //then
        assertEquals(ResponseStatus.FAILED, response.status)
    }

    @Test
    fun `createCondition successful`() {
        //given
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }



        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                        "Unit test",
                                    "title",
                                    ConditionStatus.PENDING,
                                    Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userB }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.createCondition(CreateConditionRequest(mockCondition.proposingUser.publicWalletID,
                                                            "title",
                                                            mockCondition.contract.ContractID,
                                                            mockCondition.conditionDescription))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `createCondition condition description is empty`(){
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)


        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "title","",
                ConditionStatus.PENDING,
                Date(),).apply { contract=mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userB }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.createCondition(CreateConditionRequest(mockCondition.proposingUser.publicWalletID,
                "title",
                mockCondition.contract.ContractID,
                mockCondition.conditionDescription))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `createCondition condition proposed user is empty`(){
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)


        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "title","",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.createCondition(CreateConditionRequest("",
                "title",
                mockCondition.contract.ContractID,
                mockCondition.conditionDescription))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `createCondition Agreement does not exist`(){
        //given
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)


        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "title","Unit test",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(mockCondition.contract.ContractID)).thenReturn(false)

        //when
        val response = negotiationService.createCondition(CreateConditionRequest(mockCondition.proposingUser.publicWalletID,
                                                                                    "title",
                                                                                    mockCondition.contract.ContractID,
                                                                                    mockCondition.conditionDescription))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }



    @Test
    fun `createCondition condition proposed user is not part of agreement`(){
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)


        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "title","Unit test",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.createCondition(CreateConditionRequest("Incorrect user",
                "title",
                mockCondition.contract.ContractID,
                mockCondition.conditionDescription))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAgreementDetails successful with condition`() {
        //given
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                "title","Unit test",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        val agreementUser = ArrayList<User>()
        agreementUser.add(userA)
        agreementUser.add(userB)

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(userRepository.getUsersByAgreementsContaining(mockAgreement)).thenReturn(agreementUser)

        //when
        val response = negotiationService.getAgreementDetails(GetAgreementDetailsRequest(mockAgreement.ContractID))

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

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(userRepository.getUsersByAgreementsContaining(mockAgreement)).thenReturn(agreementUser)

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
        var mockAgreementA = Agreements(UUID.randomUUID(),
                                        CreatedDate = Date(),
                                        MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockConditionA = Conditions(conditionAUUID,"title","",ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreementA }

        mockConditionA = mockConditionA.apply { proposingUser = userA }

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
        var mockAgreementA = Agreements(UUID.randomUUID(),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockConditionA = Conditions(conditionAUUID,"title","",ConditionStatus.REJECTED,
                Date(),).apply { contract = mockAgreementA }

        mockConditionA = mockConditionA.apply { proposingUser = userA }

        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)
        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

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
        var mockAgreementA = Agreements(UUID.randomUUID(),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        val conditionARequest = RejectConditionRequest(conditionAUUID)
        var mockConditionA = Conditions(conditionAUUID,"title","",ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockConditionA = mockConditionA.apply { proposingUser = userA }

        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)
        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.rejectCondition(conditionARequest)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAllConditions successful with conditions`() {
        //given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockConditionA = Conditions(conditionAUUID,"title","",ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockConditionA = mockConditionA.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(true)
        whenever(agreementsRepository.getById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(mockAgreementA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)


        //when
        val response = negotiationService.getAllConditions(GetAllConditionsRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getAllConditions successful without conditions`() {
        //given
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        whenever(agreementsRepository.existsById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(true)
        whenever(agreementsRepository.getById(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"))).thenReturn(mockAgreementA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

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
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                                    "title","Payment of 500",
                                                ConditionStatus.ACCEPTED,
                                                Date(),).apply { contract = mockAgreementA }

        mockPaymentCondition = mockPaymentCondition.apply { proposingUser = userA }

        var mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                                                "title","Duration of " + Duration.ofDays(50).seconds.toString(),
                                                ConditionStatus.ACCEPTED,
                                                Date(),).apply { contract = mockAgreementA }

        mockDurationCondition = mockDurationCondition.apply { proposingUser = userB }

        var mockRejectedCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                                                    "title","Reject this condition",
                                                    ConditionStatus.REJECTED,
                                                    Date(),).apply { contract = mockAgreementA }

        mockRejectedCondition = mockRejectedCondition.apply { proposingUser = userA }

        /*val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockRejectedCondition)

        mockAgreementA.conditions = conditionsList*/

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID
        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(conditionsRepository.getById(mockDurationCondition.conditionID)).thenReturn(mockDurationCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

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
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "title",
                "Payment of 500",
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockPaymentCondition = mockPaymentCondition.apply { proposingUser = userA }

        var mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "title","Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockDurationCondition = mockDurationCondition.apply { proposingUser = userB }

        var mockPendingCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "title","Reject this condition",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreementA }

        mockPendingCondition = mockPendingCondition.apply { proposingUser = userA }

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
        whenever(conditionsRepository.getById(mockDurationCondition.conditionID)).thenReturn(mockDurationCondition)
        whenever(conditionsRepository.getById(mockPendingCondition.conditionID)).thenReturn(mockPendingCondition)
        whenever(conditionsRepository.getAllByContract(mockAgreementA)).thenReturn(mockAgreementA.conditions)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement duration is not set`() {
        //given
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "title","Payment of 500",
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockPaymentCondition = mockPaymentCondition.apply { proposingUser = userA }

        var mockPendingCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "title","Reject this condition",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreementA }

        mockPendingCondition = mockPendingCondition.apply { proposingUser = userB }

        /*val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockPendingCondition)

        mockAgreementA.conditions = conditionsList*/

        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement payment not set`() {
        //given
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "title","Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockDurationCondition = mockDurationCondition.apply { proposingUser = userA }

        var mockPendingCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "title","Reject this condition",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreementA }

        mockPendingCondition = mockPendingCondition.apply { proposingUser = userB }

        /*val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPendingCondition)

        mockAgreementA.conditions = conditionsList*/

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockDurationCondition.conditionID)).thenReturn(mockDurationCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a payment condition is pending`() {
        //given
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "title","Payment of 500",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreementA }

        mockPaymentCondition = mockPaymentCondition.apply { proposingUser = userA }

        var mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "title","Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockDurationCondition = mockDurationCondition.apply { proposingUser = userB }

        var mockOtherCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "title","Reject this condition",
                ConditionStatus.REJECTED,
                Date(),).apply { contract = mockAgreementA }

        mockOtherCondition = mockOtherCondition.apply { proposingUser = userA }

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
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a duration condition is pending`() {
        //given
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "title","Payment of 500",
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockPaymentCondition = mockPaymentCondition.apply { proposingUser = userA }

        var mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "title","Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreementA }

        mockDurationCondition = mockDurationCondition.apply { proposingUser = userB }

        var mockOtherCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "title","Reject this condition",
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockOtherCondition = mockOtherCondition.apply { proposingUser = userA }

        /*val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockOtherCondition)

        mockAgreementA.conditions = conditionsList*/

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID
        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(conditionsRepository.getById(mockDurationCondition.conditionID)).thenReturn(mockDurationCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a payment condition is rejected`() {
        //given
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "title","Payment of 500",
                ConditionStatus.REJECTED,
                Date(),).apply { contract = mockAgreementA }

        mockPaymentCondition = mockPaymentCondition.apply { proposingUser = userA }

        var mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
                "title","Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

       mockDurationCondition = mockDurationCondition.apply { proposingUser = userB }

        var mockOtherCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
                "title","Reject this condition",
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockOtherCondition = mockOtherCondition.apply { proposingUser = userA }

        /*val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockOtherCondition)

        mockAgreementA.conditions = conditionsList*/

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID
        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(conditionsRepository.getById(mockDurationCondition.conditionID)).thenReturn(mockDurationCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `sealAgreement a duration condition is rejected`() {
        //given
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockPaymentCondition = Conditions(UUID.fromString("aa3db9c2-ff26-47c2-b14e-e9ab9af1c7ce"),
                "title","Payment of 500",
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockPaymentCondition = mockPaymentCondition.apply { proposingUser = userA }

        var mockDurationCondition = Conditions(UUID.fromString("0e7cdc2d-b0e0-4ecf-8c5c-16b503edd8b2"),
            "title","Duration of " + Duration.ofDays(50).seconds.toString(),
                ConditionStatus.REJECTED,
                Date(),).apply { contract = mockAgreementA }

        mockDurationCondition = mockDurationCondition.apply { proposingUser = userB }

        var mockOtherCondition = Conditions(UUID.fromString("76a06d5e-874f-4217-aea7-5368932e1712"),
            "title","Reject this condition",
                ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockOtherCondition = mockOtherCondition.apply { proposingUser = userA }

        /*val conditionsList = ArrayList<Conditions>()
        conditionsList.add(mockDurationCondition)
        conditionsList.add(mockPaymentCondition)
        conditionsList.add(mockOtherCondition)

        mockAgreementA.conditions = conditionsList*/

        mockAgreementA.DurationConditionUUID = mockDurationCondition.conditionID
        mockAgreementA.PaymentConditionUUID = mockPaymentCondition.conditionID

        whenever(agreementsRepository.existsById(mockAgreementA.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreementA.ContractID)).thenReturn(mockAgreementA)
        whenever(conditionsRepository.getById(mockPaymentCondition.conditionID)).thenReturn(mockPaymentCondition)
        whenever(conditionsRepository.getById(mockDurationCondition.conditionID)).thenReturn(mockDurationCondition)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreementA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.sealAgreement(SealAgreementRequest(mockAgreementA.ContractID))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getConditionDetails successful`(){
        //given
        val conditionAUUID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937")
        var mockAgreementA = Agreements(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreementA = mockAgreementA.apply { users.add(userA) }
        mockAgreementA = mockAgreementA.apply { users.add(userB) }

        var mockConditionA = Conditions(conditionAUUID,"title","",ConditionStatus.ACCEPTED,
                Date(),).apply { contract = mockAgreementA }

        mockConditionA = mockConditionA.apply { proposingUser = userA }

        whenever(conditionsRepository.existsById(conditionAUUID)).thenReturn(true)
        whenever(conditionsRepository.getById(conditionAUUID)).thenReturn(mockConditionA)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userB)

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
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Payment of 500.0",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.setPaymentCondition(SetPaymentConditionRequest(mockCondition.proposingUser.publicWalletID,
                mockCondition.contract.ContractID,
                500.0))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `setPayment Agreement does not exist`(){
        //given
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Unit test",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(mockCondition.contract.ContractID)).thenReturn(false)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.setPaymentCondition(SetPaymentConditionRequest(mockCondition.proposingUser.publicWalletID,
                mockCondition.contract.ContractID,
                500.0))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setPayment Payment is a negative value`(){
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Payment of -500.0",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.setPaymentCondition(SetPaymentConditionRequest(mockCondition.proposingUser.publicWalletID,
                mockCondition.contract.ContractID,
                -500.0))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setPayment condition proposed user is empty`(){
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Unit test",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.setPaymentCondition(SetPaymentConditionRequest("",
                mockCondition.contract.ContractID,
                500.0))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setPayment proposed user is not part of agreement`(){
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Payment of 500.0",
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.setPaymentCondition(SetPaymentConditionRequest("Invalid user",
                mockCondition.contract.ContractID,
                500.0))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setDurationCondition successful`(){
        //given
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Duration of " + Duration.ofSeconds(500).seconds,
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.setDurationCondition(SetDurationConditionRequest(mockCondition.proposingUser.publicWalletID,
                mockCondition.contract.ContractID,
                Duration.ofSeconds(500)))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `setDuration Agreement does not exist`(){
        //given
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Duration of " + Duration.ofSeconds(500).seconds,
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }



        whenever(agreementsRepository.existsById(mockCondition.contract.ContractID)).thenReturn(false)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.setDurationCondition(SetDurationConditionRequest(mockCondition.proposingUser.publicWalletID,
                mockCondition.contract.ContractID,
                Duration.ofSeconds(500)))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setDuration Duration is a negative value`(){
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Duration of " + Duration.ofSeconds(-500).seconds,
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)

        //when
        val response = negotiationService.setDurationCondition(SetDurationConditionRequest(mockCondition.proposingUser.publicWalletID,
                mockCondition.contract.ContractID,
                Duration.ofSeconds(-500)))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setDuration condition proposed user is empty`(){
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        val mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Duration of " + Duration.ofSeconds(500).seconds,
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.setDurationCondition(SetDurationConditionRequest("",
                mockCondition.contract.ContractID,
                Duration.ofSeconds(500)))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `setDuration proposed user is not part of agreement`(){
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
                CreatedDate = Date(),
                MovedToBlockChain = false)

        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        var mockCondition = Conditions(UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            "title","Duration of " + Duration.ofSeconds(500).seconds,
                ConditionStatus.PENDING,
                Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = userA }

        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userB)

        //when
        val response = negotiationService.setDurationCondition(SetDurationConditionRequest("Invalid user",
                mockCondition.contract.ContractID,
                Duration.ofSeconds(500)))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }
}