package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.security

import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.security.requests.AddUserRequest
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
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
class GetNonce {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userRepository: UserRepository


    private lateinit var userA: User

    @BeforeEach
    fun beforeEach() {
        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")

        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.existsById("0xf8Bdb506E7A1569B5CEf4999c4f7D71AA5CdF18e")).thenReturn(false)
    }

    private fun requestSender(
        userId: String
    ): MockHttpServletResponse {
        return mockMvc.perform(MockMvcRequestBuilders.get("/user/${userId}")).andReturn().response
    }

    @Test
    fun `Get nonce successful test`() {
        val response = requestSender(userA.publicWalletID)
        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `Get nonce Failed test user does not exist`() {
        val response = requestSender("0xf8Bdb506E7A1569B5CEf4999c4f7D71AA5CdF18e")
        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
}