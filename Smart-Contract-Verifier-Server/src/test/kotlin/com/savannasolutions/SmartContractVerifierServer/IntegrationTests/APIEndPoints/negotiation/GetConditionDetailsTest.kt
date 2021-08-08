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
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
class GetConditionDetailsTest {
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
    private lateinit var conditionUUID: UUID

    @BeforeEach
    fun beforeEach()
    {

        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")

        val agreement = Agreements(
            UUID.fromString("3c5657d6-e302-48d3-b9df-dcfccec97503"),
            "test agreement",
            "test description",
            CreatedDate = Date(),
            MovedToBlockChain = false)

        val condition = Conditions(
            UUID.fromString("967ee13c-dd5d-4de5-adb5-7dd4907fb2cf"),
            "Test conditions pending",
            "Test condition description",
            ConditionStatus.PENDING,
            Date()
        )
        conditionUUID = condition.conditionID
        condition.proposingUser = userB
        condition.contract = agreement

        whenever(conditionsRepository.existsById(conditionUUID)).thenReturn(true)
        whenever(conditionsRepository.getById(conditionUUID)).thenReturn(condition)
    }

    private fun requestSender(rjson: String) : MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/negotiation/get-condition-details")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)).andReturn().response
    }

    @Test
    fun `GetConditionDetails successful`()
    {
        val rjson = "{\"ConditionID\" : \"${conditionUUID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, conditionUUID.toString())
    }

    @Test
    fun `GetConditionDetails failure due to condition not existing`()
    {
        val rjson = "{\"ConditionID\" : \"d763f385-c91a-4a3b-ac95-25dcfc22afe9\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }


}