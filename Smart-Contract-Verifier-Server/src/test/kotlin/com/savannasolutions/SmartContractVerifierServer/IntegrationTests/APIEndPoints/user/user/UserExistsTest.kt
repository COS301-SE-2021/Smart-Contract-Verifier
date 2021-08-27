package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.user.user

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
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
class UserExistsTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var agreementsRepository: AgreementsRepository

    @MockBean
    lateinit var conditionsRepository: ConditionsRepository

    private lateinit var userA: User

    @BeforeEach
    fun beforeEach()
    {
        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")

        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
    }

    private fun requestSender(rjson: String): MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/user-exists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)
        ).andReturn().response
    }

    @Test
    fun `UserExists successful user does exist`()
    {
        val rjson = "{\"WalletID\" : \"${userA.publicWalletID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, ":true")
    }

    @Test
    fun `UserExists successful user does not exist`()
    {
        val rjson = "{\"WalletID\" : \"other user\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, ":false")
    }

    @Test
    fun `UserExists failed wallet id is empty`()
    {
        val rjson = "{\"WalletID\" : \"\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
}