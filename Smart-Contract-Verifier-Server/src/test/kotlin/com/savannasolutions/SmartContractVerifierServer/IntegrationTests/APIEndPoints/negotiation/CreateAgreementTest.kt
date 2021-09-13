package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.CreateAgreementRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.responses.CreateAgreementResponse
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
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
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs("docs/api/post/user/userID/agreement")
class CreateAgreementTest {
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

        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(agreementsRepository.save(any<Agreements>())).thenReturn(agreement)
    }

    private fun requestSender(rjson: String,
                              userID: String,
                              responseFieldDescriptors: ArrayList<FieldDescriptor>,
                              testName: String) : MockHttpServletResponse
    {
        return mockMvc.perform(MockMvcRequestBuilders.post("/user/${userID}/agreement")
            .contentType(MediaType.APPLICATION_JSON)
            .content(rjson)).andDo(
            MockMvcRestDocumentation.document(
                testName,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(responseFieldDescriptors),
                PayloadDocumentation.requestFields(CreateAgreementRequest.request())
            )
        ).andReturn().response
    }


    @Test fun `CreateAgreement successful`(){
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiResponse())
        fieldDescriptorResponse.addAll(CreateAgreementResponse.response())
        //End of documentation


        val rjson = "{\"AgreementDescription\":\"test description\",\"AgreementImageURL\":\"http.dodgyurl.com\",\"PartyB\":\"0x37Ec9a8aBFa094b24054422564e68B08aF3114B4\",\"AgreementTitle\":\"test agreement\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            fieldDescriptorResponse,
            "CreateAgreement successful")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, "\"AgreementID\":\"3c5657d6-e302-48d3-b9df-dcfccec97503\"")
    }

    @Test
    fun `CreateAgreement failed when party B is empty`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation


        val rjson = "{\"AgreementDescription\":\"test description\",\"AgreementImageURL\":\"http.dodgyurl.com\",\"PartyB\":\"\",\"AgreementTitle\":\"test agreement\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            fieldDescriptorResponse,
            "CreateAgreement failed when party B is empty")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `CreateAgreement failed when Agreement description is empty`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation


        val rjson = "{\"AgreementDescription\":\"\",\"AgreementImageURL\":\"http.dodgyurl.com\",\"PartyB\":\"0x37Ec9a8aBFa094b24054422564e68B08aF3114B4\",\"AgreementTitle\":\"test agreement\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            fieldDescriptorResponse,
            "CreateAgreement failed when agreement description is empty")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `CreateAgreement failed when Agreement title is empty`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"AgreementDescription\":\"test description\",\"AgreementImageURL\":\"http.dodgyurl.com\",\"PartyB\":\"0x37Ec9a8aBFa094b24054422564e68B08aF3114B4\",\"AgreementTitle\":\"\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            fieldDescriptorResponse,
            "CreateAgreement failed when agreement title is empty")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `CreateAgreement userA is equal to userB`(){
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"AgreementDescription\":\"test description\",\"AgreementImageURL\":\"http.dodgyurl.com\",\"PartyB\":\"0x743Fb032c0bE976e1178d8157f911a9e825d9E23\",\"AgreementTitle\":\"test agreement\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            fieldDescriptorResponse,
            "CreateAgreement userA is equal to userB")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `CreateAgreement userB does not exist`(){
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"AgreementDescription\":\"test description\",\"AgreementImageURL\":\"http.dodgyurl.com\",\"PartyB\":\"does not exist\",\"AgreementTitle\":\"test agreement\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            fieldDescriptorResponse,
            "CreateAgreement userB does not exist")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
}