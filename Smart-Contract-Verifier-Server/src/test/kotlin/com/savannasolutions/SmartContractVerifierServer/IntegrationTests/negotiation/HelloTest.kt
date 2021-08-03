package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.negotiation

import com.savannasolutions.SmartContractVerifierServer.negotiation.controllers.NegotiationController
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloTest{

    @Autowired
    lateinit var negotiationController: NegotiationController

    @Test
    fun `Call hello`()
    {
        val response = negotiationController.hello()
        assertEquals(response, "HELLO!")
    }
}