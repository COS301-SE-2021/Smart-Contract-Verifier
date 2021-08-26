package com.savannasolutions.SmartContractVerifierServer.UnitTests.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.CreateAgreementRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.responses.CreateAgreementResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

internal class CreateAgreementUnitTests {
    private val conditionsRepository : ConditionsRepository = mock()
    private val agreementsRepository : AgreementsRepository = mock()
    private val userRepository : UserRepository = mock()
    private val judgesRepository : JudgesRepository = mock()
    private val negotiationService = NegotiationService(agreementsRepository, conditionsRepository, userRepository, judgesRepository)

    private val userAID = "0x743Fb032c0bE976e1178d8157f911a9e825d9E23"
    private val userBID = "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4"

    private fun parameterizedCreateAgreement(partyA: String,
                                             partyAExist: Boolean,
                                             partyB: String,
                                             partyBExist: Boolean): CreateAgreementResponse
    {
        //given
        var mockAgreement = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
            CreatedDate = Date(),
            MovedToBlockChain = false)

        val tempUserA = User(partyA)
        val tempUserB = User(partyB)

        mockAgreement = mockAgreement.apply { users.add(tempUserA)}
        mockAgreement = mockAgreement.apply { users.add(tempUserB)}

        //when
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(mockAgreement)
        whenever(userRepository.existsById(partyA)).thenReturn(partyAExist)
        whenever(userRepository.existsById(partyB)).thenReturn(partyBExist)
        whenever(userRepository.getById(partyA)).thenReturn(tempUserA)
        whenever(userRepository.getById(partyB)).thenReturn(tempUserB)

        //then
        return negotiationService.createAgreement(partyA, CreateAgreementRequest(partyB,
                                                                                "test",
                                                                                "test description",
                                                                                "image.com")
        )
    }

    @Test
    fun `createAgreement successful`() {


        //when
        val response = parameterizedCreateAgreement(userAID,true,userBID, true)

        //then
        Assertions.assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `createAgreement Party A is empty`(){
        //given

        //when
        val response = parameterizedCreateAgreement("",true,userBID,true)

        //then
        Assertions.assertEquals(ResponseStatus.FAILED, response.status)
    }

    @Test
    fun `createAgreement Party B is empty`(){
        //given

        //when
        val response = parameterizedCreateAgreement(userAID,true,"",true)

        //then
        Assertions.assertEquals(ResponseStatus.FAILED, response.status)
    }

    @Test
    fun `createAgreement Party A and B are the same`(){
        //given

        //when
        val response = parameterizedCreateAgreement(userAID,true,userAID,true)

        //then
        Assertions.assertEquals(ResponseStatus.FAILED, response.status)
    }

    @Test
    fun `createAgreement Party A does not exist`(){
        //given

        //when
        val response = parameterizedCreateAgreement(userAID,false,userBID,true)

        //then
        Assertions.assertEquals(ResponseStatus.FAILED, response.status)
    }

    @Test
    fun `createAgreement Party B does not exist`(){
        //given

        //when
        val response = parameterizedCreateAgreement(userAID,true,userBID,false)

        //then
        Assertions.assertEquals(ResponseStatus.FAILED, response.status)
    }
}