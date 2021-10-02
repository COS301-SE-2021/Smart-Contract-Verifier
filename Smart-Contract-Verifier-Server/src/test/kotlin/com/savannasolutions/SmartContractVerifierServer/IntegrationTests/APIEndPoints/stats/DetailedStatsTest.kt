package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.stats

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.contracts.models.Judges
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.stats.responses.DetailedStatsResponse
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
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "docs/api/get/stats/detailed")
class DetailedStatsTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var agreementsRepository: AgreementsRepository

    @MockBean
    lateinit var judgesRepository: JudgesRepository

    private fun requestSender(responseFieldDescriptors: ArrayList<FieldDescriptor>, startDate : Date, endDate : Date, testName : String)
            : MockHttpServletResponse {
        val sd = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val ed = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return mockMvc.perform(
            MockMvcRequestBuilders.get("/stats/detailed")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"StartDate\":\"${sd}\",\"EndDate\":\"${ed}\"}")
        ).andDo(
            MockMvcRestDocumentation.document(
                testName,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(responseFieldDescriptors)
            )
        ).andReturn().response
    }

    private lateinit var mockAgreementBC : Agreements
    private lateinit var mockAgreementNBC : Agreements
    private lateinit var startDateCorrect : Date
    private lateinit var startDateIncorrect : Date
    private lateinit var endDateCorrect : Date
    private lateinit var endDateIncorrect : Date

    @BeforeEach
    fun beforeEach()
    {
        //given
        startDateCorrect = Date()
        endDateIncorrect = Date()
        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")
        mockAgreementBC = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
            CreatedDate = Date(),
            MovedToBlockChain = true)

        val userList = ArrayList<User>()
        userList.add(userA)
        userList.add(userB)
        val judge = Judges()

        mockAgreementBC = mockAgreementBC.apply { users.add(userA) }
        mockAgreementBC.apply { users.add(userB) }

        mockAgreementNBC = Agreements(ContractID = UUID.fromString("16e92cf0-31b1-4617-abdc-625ec01b7f1d"),
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

        endDateCorrect = Date()
        startDateIncorrect = Date()

        whenever(agreementsRepository.countAgreementsByCreatedDateBefore(endDateCorrect)).thenReturn(2)
        whenever(agreementsRepository.countAgreementsBySealedDateBefore(endDateCorrect)).thenReturn(1)
        whenever(agreementsRepository.countAgreementsByCreatedDateBefore(startDateCorrect)).thenReturn(0)
        whenever(agreementsRepository.countAgreementsBySealedDateBefore(startDateCorrect)).thenReturn(0)
        whenever(agreementsRepository.countAgreementsByCreatedDateBetween(startDateCorrect, endDateCorrect)).thenReturn(2)
        whenever(agreementsRepository.countAgreementsBySealedDateBetween(startDateCorrect, endDateCorrect)).thenReturn(2)
    }

    @Test
    fun `DetailedStats api test successful`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiResponse())
        fieldDescriptorResponse.addAll(DetailedStatsResponse.response())
        //End of documentation

        val response = requestSender(fieldDescriptorResponse, startDateCorrect, endDateCorrect, "DetailedStats api test successful")

        assertContains(response.contentAsString, "SUCCESSFUL")
    }

    @Test
    fun `DetailedStats api test failed`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val ed = Date.from(
            (endDateIncorrect.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(-1)).atStartOfDay(
                ZoneId.systemDefault()).toInstant())

        val response = requestSender(fieldDescriptorResponse, startDateIncorrect, ed, "DetailedStats api test successful")

        assertContains(response.contentAsString, "FAILED")
    }

}