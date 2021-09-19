package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.security

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
class LoginTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userRepository: UserRepository


    private lateinit var userA: User

    @BeforeEach
    fun beforeEach() {
        userA = User("0x455f6a91d7364a54c42718bEb2262C880E4E0a60")
        userA.nonce = 1234567891
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.existsById("0xf8Bdb506E7A1569B5CEf4999c4f7D71AA5CdF18e")).thenReturn(false)
    }

    private fun requestSender(
        userId: String,
        r_json: String
    ): MockHttpServletResponse {
        return mockMvc.perform(MockMvcRequestBuilders.post("/user/${userId}").content(
            r_json
        ).contentType(MediaType.APPLICATION_JSON)).andReturn().response
    }

    @Test
    fun `Login successful test`() {
        val response = requestSender("0x455f6a91d7364a54c42718bEb2262C880E4E0a60", "{\"SignedNonce\":\"0xab49d6172ddba98ecaab3fc5271be82c2db9237359ca28007969c33631d13c054fd873114d74cd21ff9bf7b58d0a0b78fa4f5f7e925a9011e698b87ae8eb68201b\"}")
        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `Login Failed test user does not exist`() {
        val response = requestSender("0xf8Bdb506E7A1569B5CEf4999c4f7D71AA5CdF18e", "{\"SignedNonce\":\"failure\"}")
        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `Login Failed test user failed challenge`() {
        val response = requestSender(userA.publicWalletID, "{\"SignedNonce\":\"0x5f9d0dff277fea3433b802254a50b65e367f78adb943ac03e3250c3807da8f7f2e4c7533864f67683ce07cfe790ec425c082504fefbcbfb6924cd8229646f4b21b\"}")
        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
}