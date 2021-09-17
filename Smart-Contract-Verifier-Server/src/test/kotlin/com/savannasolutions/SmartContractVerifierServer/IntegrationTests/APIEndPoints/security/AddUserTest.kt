package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.user.user

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
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
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "docs/api/post/user")
class AddUserTest {
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

        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(false)
        whenever(userRepository.existsById("broken")).thenReturn(true)
        whenever(userRepository.save(any<User>())).thenReturn(userA)
    }

    private fun requestSender(rjson: String, responseFieldDescriptors: ArrayList<FieldDescriptor>,
                              testName: String): MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)
        ).andDo(
            MockMvcRestDocumentation.document(
                testName,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(responseFieldDescriptors),
                PayloadDocumentation.requestFields(AddUserRequest.request())
            )
        ).andReturn().response
    }

    @Test
    fun `AddUser successful`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiEmptyResponse())
        //End of documentation

        val rjson = "{\"WalletID\" : \"${userA.publicWalletID}\"," +
                "    \"Alias\" : \"test\"}"

        val response = requestSender(rjson, fieldDescriptorResponse, "AddUser successful")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `AddUser failed user exists`()
    {
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"WalletID\" : \"broken\"," +
                "    \"Alias\" : \"test\"}"

        val response = requestSender(rjson, fieldDescriptorResponse, "AddUser failed user exists")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
}