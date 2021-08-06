package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.negotiation

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertContains
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class CreateConditionTest {
    @Autowired
    lateinit var mockMvc : MockMvc

    @MockBean
    lateinit var agreementsRepository : AgreementsRepository

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var conditionsRepository: ConditionsRepository

    private lateinit var userA : User
    private lateinit var userB : User
    private lateinit var userC : User
    private lateinit var agreement : Agreements

    @BeforeEach
    fun beforeEach()
    {

        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")
        userC = User("0x69Ec9a8aBFa094b24054422564e68B08aF311400")


        agreement = Agreements(
            UUID.fromString("3c5657d6-e302-48d3-b9df-dcfccec97503"),
            "test agreement",
            "test description",
            CreatedDate = Date(),
            MovedToBlockChain = false)

        val condition = Conditions(UUID.fromString("967ee13c-dd5d-4de5-adb5-7dd4907fb2cf"),
            "Test conditions pending",
            "Test condition description",
            ConditionStatus.PENDING,
            Date())

        val userList = ArrayList<User>()
        userList.add(userA)
        userList.add(userB)

        agreement.users.add(userA)
        agreement.users.add(userB)


        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(userList)
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(condition)
    }

    private fun requestSender(rjson: String) : MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/negotiation/create-condition")
            .contentType(MediaType.APPLICATION_JSON)
            .content(rjson)).andReturn().response
    }

    @Test
    fun `CreateCondition successful`(){
        val rjson = "{\n" +
                "    \"ProposedUser\" : \"${userA.publicWalletID}\", \n" +
                "    \"ConditionTitle\" : \"Test\",\n" +
                "    \"AgreementID\" : \"${agreement.ContractID}\",\n" +
                "    \"ConditionDescription\" : \"This is a postman test\"\n" +
                "}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `CreateCondition failed agreement does not exist`(){
        val rjson = "{\n" +
                "    \"ProposedUser\" : \"${userA.publicWalletID}\", \n" +
                "    \"ConditionTitle\" : \"Test\",\n" +
                "    \"AgreementID\" : \"6968236f-6020-4ed5-8167-0941648c5365\",\n" +
                "    \"ConditionDescription\" : \"This is a postman test\"\n" +
                "}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"status\":\"FAILED\"")
    }

    @Test
    fun `CreateCondition failed condition description `(){
        val rjson = "{\n" +
                "    \"ProposedUser\" : \"${userA.publicWalletID}\", \n" +
                "    \"ConditionTitle\" : \"Test\",\n" +
                "    \"AgreementID\" : \"${agreement.ContractID}\",\n" +
                "    \"ConditionDescription\" : \"\"\n" +
                "}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"status\":\"FAILED\"")
    }

    @Test
    fun `CreateCondition failed when proposed user is empty`(){
        val rjson = "{\n" +
                "    \"ProposedUser\" : \"\", \n" +
                "    \"ConditionTitle\" : \"Test\",\n" +
                "    \"AgreementID\" : \"${agreement.ContractID}\",\n" +
                "    \"ConditionDescription\" : \"This is a postman test\"\n" +
                "}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"status\":\"FAILED\"")
    }

    @Test
    fun `CreateCondition failed title is empty`(){
        val rjson = "{\n" +
                "    \"ProposedUser\" : \"${userA.publicWalletID}\", \n" +
                "    \"ConditionTitle\" : \"\",\n" +
                "    \"AgreementID\" : \"${agreement.ContractID}\",\n" +
                "    \"ConditionDescription\" : \"This is a postman test\"\n" +
                "}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"status\":\"FAILED\"")
    }

    @Test
    fun `CreateCondition failed user is not part of agreement`(){
        val rjson = "{\n" +
                "    \"ProposedUser\" : \"${userC.publicWalletID}\", \n" +
                "    \"ConditionTitle\" : \"Test\",\n" +
                "    \"AgreementID\" : \"${agreement.ContractID}\",\n" +
                "    \"ConditionDescription\" : \"This is a postman test\"\n" +
                "}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"status\":\"FAILED\"")
    }
}