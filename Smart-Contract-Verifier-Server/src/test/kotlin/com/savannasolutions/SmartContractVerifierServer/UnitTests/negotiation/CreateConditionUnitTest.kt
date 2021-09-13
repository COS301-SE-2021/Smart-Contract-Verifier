package com.savannasolutions.SmartContractVerifierServer.UnitTests.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.CreateConditionRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.responses.CreateConditionResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

internal class CreateConditionUnitTest {
    private val conditionsRepository : ConditionsRepository = mock()
    private val agreementsRepository : AgreementsRepository = mock()
    private val userRepository : UserRepository = mock()
    private val judgesRepository : JudgesRepository = mock()
    private val negotiationService = NegotiationService(agreementsRepository, conditionsRepository, userRepository, judgesRepository)

    private val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
    private val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")
    private val otherUser = User("other", "other")

    private fun parameterizedCreateCondition(description: String,
                                            pUser: String,
                                            agreementExist: Boolean,
                                            title: String): ApiResponse<CreateConditionResponse>
    {
        //given
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
            CreatedDate = Date(),
            MovedToBlockChain = false)

        mockAgreement = mockAgreement.apply { users.add(userA) }
        mockAgreement = mockAgreement.apply { users.add(userB) }

        val userList = ArrayList<User>()
        userList.add(userA)
        userList.add(userB)

        val propUser = User(pUser)


        var mockCondition = Conditions(
            UUID.fromString("19cda645-d398-4b24-8a3b-ab7f67a9e8f8"),
            title,
            description,
            ConditionStatus.PENDING,
            Date(),).apply { contract = mockAgreement }

        mockCondition = mockCondition.apply { proposingUser = propUser }

        //when
        whenever(agreementsRepository.existsById(mockAgreement.ContractID)).thenReturn(agreementExist)
        whenever(agreementsRepository.getById(mockAgreement.ContractID)).thenReturn(mockAgreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(mockCondition)
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.getUsersByAgreementsContaining(mockAgreement)).thenReturn(userList)

        //then
        return negotiationService.createCondition(pUser,
                                                    mockAgreement.ContractID,
                                                    CreateConditionRequest(title,description)
        )
    }

    @Test
    fun `createCondition successful`() {
        //given

        //when
        val response = parameterizedCreateCondition("description",
                                                    userA.publicWalletID,
                                                    true,
                                                    "title")

        //then
        Assertions.assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `createCondition condition description is empty`(){
        //given

        //when
        val response = parameterizedCreateCondition("",
                                                    userA.publicWalletID,
                                                    true,
                                                    "title")

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `createCondition condition proposed user is empty`(){
        //given

        //when
        val response = parameterizedCreateCondition("desc","",true,"title")

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `createCondition Agreement does not exist`(){
        //given

        //when
        val response = parameterizedCreateCondition("desc",
                                                    userA.publicWalletID,
                                                    false,
                                                    "title")

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `createCondition condition proposed user is not part of agreement`(){
        //given

        //when
        val response = parameterizedCreateCondition("desc",
                                                    otherUser.publicWalletID,
                                                    true,
                                                    "title")

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `createCondition title is empty`(){
        //given

        //when
        val response = parameterizedCreateCondition("desc",
            userA.publicWalletID,
            true,
            "")

        //then
        Assertions.assertEquals(response.status, ResponseStatus.FAILED)
    }
}