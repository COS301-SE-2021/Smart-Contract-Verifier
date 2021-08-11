package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.negotiation

import com.savannasolutions.SmartContractVerifierServer.negotiation.controllers.NegotiationController
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RunWith(SpringRunner::class)
@WebMvcTest(NegotiationController::class)
class HelloTest{

    @Autowired
    lateinit var mockMvc : MockMvc

    @MockBean
    lateinit var negotiationService: NegotiationService

    @Test
    fun `MVC call hello`()
    {
        val response = mockMvc.perform(MockMvcRequestBuilders.get("/negotiation/hello"))
        response.andExpect(MockMvcResultMatchers.status().isOk)
        response.andExpect(MockMvcResultMatchers.content().string("HELLO!"))
    }
}