package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.negotiation

import com.savannasolutions.SmartContractVerifierServer.negotiation.controllers.NegotiationController
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class CreateAgreementTest {
    @Autowired
    lateinit var mockMvc : MockMvc

    @MockBean
    lateinit var agreementsRepository : AgreementsRepository

//Remove annotation @Mock for repos

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var conditionsRepository: ConditionsRepository

    private lateinit var userA : User
    private lateinit var userB : User

    @BeforeEach
    fun beforeEach()
    {

        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")

        val agreement = Agreements(UUID.fromString("3c5657d6-e302-48d3-b9df-dcfccec97503"),
                                    "test agreement",
                                    "test description",
                                    CreatedDate = Date(),
                                    MovedToBlockChain = false)

        //whenever(userRepository) whenever stuff comes here
        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(agreement)
    }


    @Test fun `CreateAgreement successful`(){
        val rjson = "{\"AgreementDescription\":\"test description\",\"AgreementImageURL\":\"http.dodgyurl.com\",\"PartyA\":\"0x743Fb032c0bE976e1178d8157f911a9e825d9E23\",\"PartyB\":\"0x37Ec9a8aBFa094b24054422564e68B08aF3114B4\",\"AgreementTitle\":\"test agreement\"}"

        val response = mockMvc.perform(MockMvcRequestBuilders.post("/negotiation/create-agreement")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(rjson)).andReturn().response

        assertEquals(response.status, 200)
        assertContains(response.contentAsString, "\"status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, "\"agreementID\":\"3c5657d6-e302-48d3-b9df-dcfccec97503\"")
    }
}