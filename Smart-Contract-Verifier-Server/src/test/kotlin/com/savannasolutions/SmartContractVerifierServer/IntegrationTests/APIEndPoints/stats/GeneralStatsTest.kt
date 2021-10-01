package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.stats

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.contracts.models.Judges
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.security.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.stats.responses.GeneralStatsResponse
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class GeneralStatsTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var agreementsRepository: AgreementsRepository

    @MockBean
    lateinit var judgesRepository: JudgesRepository

    private fun requestSender(responseFieldDescriptors: ArrayList<FieldDescriptor>)
            : MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/stats")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
            MockMvcRestDocumentation.document(
                "General test successful",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(responseFieldDescriptors)
            )
        ).andReturn().response
    }

    @BeforeEach
    fun beforeEach()
    {
        //given
        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")
        var mockAgreementBC = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
            CreatedDate = Date(),
            MovedToBlockChain = true)

        val userList = ArrayList<User>()
        userList.add(userA)
        userList.add(userB)
        val judge = Judges()

        mockAgreementBC = mockAgreementBC.apply { users.add(userA) }
        mockAgreementBC.apply { users.add(userB) }

        var mockAgreementNBC = Agreements(ContractID = UUID.fromString("16e92cf0-31b1-4617-abdc-625ec01b7f1d"),
            CreatedDate = Date(),
            MovedToBlockChain = false,
            SealedDate = Date()
        )

        mockAgreementNBC = mockAgreementNBC.apply { users.add(userA) }
        mockAgreementNBC = mockAgreementNBC.apply { users.add(userB) }

        val list = ArrayList<Agreements>()
        list.add(mockAgreementNBC)

        val judgeList = ArrayList<Judges>()
        judgeList.add(judge)

        //when
        whenever(agreementsRepository.countAgreements()).thenReturn(2)
        whenever(agreementsRepository.countAgreementsByBlockchainIDNotNull()).thenReturn(1)
        whenever(agreementsRepository.countAgreementsByBlockchainIDNull()).thenReturn(1)
        whenever(agreementsRepository.getAllBySealedDateNotNull()).thenReturn(list)
        whenever(judgesRepository.getAllByAgreement(mockAgreementNBC)).thenReturn(judgeList)
        whenever(userRepository.countAll()).thenReturn(3)
        whenever(judgesRepository.countAll()).thenReturn(1)
    }

    @Test
    fun `GeneralStats successful`(){
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiEmptyResponse())
        fieldDescriptorResponse.addAll(GeneralStatsResponse.response())
        //End of documentation

        val response = requestSender(fieldDescriptorResponse)

        assertContains(response.contentAsString, "SUCCESS")
        //TODO: UPDATE THE SECURITY FILTERS
    }

}