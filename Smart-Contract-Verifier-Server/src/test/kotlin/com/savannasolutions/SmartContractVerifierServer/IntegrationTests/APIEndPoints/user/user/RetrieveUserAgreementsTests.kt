package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.user.user

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
import kotlin.collections.ArrayList
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
class GetAgreementDetail {
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
    private lateinit var acceptedConditionID: UUID
    private lateinit var rejectedConditionID: UUID
    private lateinit var pendingConditionID: UUID
    private lateinit var agreementUUID : UUID

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
        agreementUUID = agreement.ContractID

        val pendingCondition = Conditions(
            UUID.fromString("967ee13c-dd5d-4de5-adb5-7dd4907fb2cf"),
            "Test conditions pending",
            "Test condition description",
            ConditionStatus.PENDING,
            Date()
        )
        pendingConditionID = pendingCondition.conditionID
        pendingCondition.proposingUser = userB

        val acceptedCondition = Conditions(
            UUID.fromString("d20088ad-6e94-426f-84f8-bf01b9e9bf6e"),
            "Test condition accepted",
            "Test condition description",
            ConditionStatus.ACCEPTED,
            Date()
        )
        acceptedConditionID = acceptedCondition.conditionID
        acceptedCondition.proposingUser = userA

        val rejectedCondition = Conditions(
            UUID.fromString("e2526862-6282-4fc9-9ea7-f6dbc6064c48"),
            "Test condition rejected",
            "Test condition description",
            ConditionStatus.REJECTED,
            Date()
        )
        rejectedConditionID = rejectedCondition.conditionID
        rejectedCondition.proposingUser = userA

        val conditionList = ArrayList<Conditions>()
        conditionList.add(pendingCondition)
        conditionList.add(acceptedCondition)
        conditionList.add(rejectedCondition)

        val userList = ArrayList<User>()
        userList.add(userA)
        userList.add(userB)

        val agreementsList = mutableSetOf<Agreements>()
        agreementsList.add(agreement)


        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(agreementsRepository.getAllByUsersContaining(userA)).thenReturn(agreementsList)
        whenever(conditionsRepository.getAllByContract(agreement)).thenReturn(conditionList)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(userList)
    }

    private fun requestSender(userID: String) : MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.get("/user/${userID}/agreement")
                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn().response
    }

    @Test
    fun `RetrieveUserAgreementsTests successful`()
    {
        val response = requestSender(userA.publicWalletID)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, agreementUUID.toString())
        assertContains(response.contentAsString, pendingConditionID.toString())
        assertContains(response.contentAsString, acceptedConditionID.toString())
        assertContains(response.contentAsString, rejectedConditionID.toString())
    }

    @Test
    fun `RetrieveUserAgreementsTests failed due to user not existing`()
    {
        val response = requestSender("other user")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
}