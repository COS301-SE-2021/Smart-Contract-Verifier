package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.negotiation

import com.savannasolutions.SmartContractVerifierServer.negotiation.controllers.NegotiationController
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.assertEquals

//@RunWith(SpringRunner::class)
//@WebMvcTest(NegotiationController::class)
@ExtendWith(MockitoExtension::class)
class CreateAgreementTest {
    //@Autowired
    lateinit var mockMvc : MockMvc

    //@MockBean
    //lateinit var negotiationService: NegotiationService

    //@MockBean
    //lateinit var agreementsRepository: AgreementsRepository
    @Mock
    var agreementsRepository : AgreementsRepository = mock()

    //@MockBean
    //lateinit var userRepository: UserRepository
    @Mock
    var userRepository : UserRepository = mock()

    //@MockBean
    //lateinit var conditionsRepository: ConditionsRepository
    @Mock
    var conditionsRepository : ConditionsRepository = mock()

    @Mock
    var negotiationService = NegotiationService(agreementsRepository, conditionsRepository, userRepository)

    @InjectMocks
    lateinit var negotiationController: NegotiationController

    private lateinit var userA : User
    private lateinit var userB : User

    @BeforeEach
    fun beforeEach()
    {

        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")

        mockMvc = MockMvcBuilders.standaloneSetup(negotiationController).build()

    }


    @Test fun `CreateAgreement successful`(){
        val rjson = "{\"AgreementDescription\":\"test description\",\"AgreementImageURL\":\"http.dodgyurl.com\",\"PartyA\":\"0x743Fb032c0bE976e1178d8157f911a9e825d9E23\",\"PartyB\":\"0x37Ec9a8aBFa094b24054422564e68B08aF3114B4\",\"AgreementTitle\":\"test agreement\"}"

        val response = mockMvc.perform(MockMvcRequestBuilders.post("/negotiation/create-agreement")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(rjson)).andReturn().response

        assertEquals(response.status, 200)
    }
}