package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.negotiation

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.CreateConditionRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.responses.CreateConditionResponse
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
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
@AutoConfigureRestDocs(outputDir = "docs/api/post/user/userID/agreement/agreementID/condition")
class CreateConditionTest {
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
    private lateinit var userC : User
    private lateinit var agreement : Agreements

    @BeforeEach
    fun beforeEach()
    {

        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")
        userC = User("0x69Ec9a8aBFa094b24054422564e68B08aF311400")


        agreement = Agreements(
            UUID.fromString("3c5657d6-e302-48d3-b9df-dcfccec97503"),
            "test agreement",
            "test description",
            CreatedDate = Date(),
            MovedToBlockChain = false)

        val condition = Conditions(UUID.fromString("967ee13c-dd5d-4de5-adb5-7dd4907fb2cf"),
            "Test conditions pending",
            "Test condition description",
            ConditionStatus.PENDING,
            Date())

        val userList = ArrayList<User>()
        userList.add(userA)
        userList.add(userB)

        agreement.users.add(userA)
        agreement.users.add(userB)


        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(userList)
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(true)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(conditionsRepository.save(any<Conditions>())).thenReturn(condition)
    }

    private fun requestSender(rjson: String,
                              userID: String,
                              agreementID: UUID,
                              responseFieldDescriptors: ArrayList<FieldDescriptor>,
                              testName: String) : MockHttpServletResponse
    {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/user/${userID}/agreement/${agreementID}/condition")
            .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "bearer ${generateToken(userID)}")
            .content(rjson)).andDo(
            MockMvcRestDocumentation.document(
                testName,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(responseFieldDescriptors),
                PayloadDocumentation.requestFields(CreateConditionRequest.request())
            )
        ).andReturn().response
    }

    @Test
    fun `CreateCondition successful`(){
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiResponse())
        fieldDescriptorResponse.addAll(CreateConditionResponse.response())
        //End of documentation

        val rjson = "{\n" +
                "    \"ConditionTitle\" : \"Test\",\n" +
                "    \"ConditionDescription\" : \"This is a postman test\"\n" +
                "}"

        val response = requestSender(rjson, userA.publicWalletID,
            agreement.ContractID,
            fieldDescriptorResponse,
            "CreateCondition successful")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `CreateCondition failed agreement does not exist`(){
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\n" +
                "    \"ConditionTitle\" : \"Test\",\n" +
                "    \"ConditionDescription\" : \"This is a postman test\"\n" +
                "}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            UUID.fromString("eb558bea-389e-4e7b-afed-4987dbf37f85"),
            fieldDescriptorResponse,
            "CreateCondition failed agreement does not exist")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `CreateCondition failed condition description `(){
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\n" +
                "    \"ConditionTitle\" : \"Test\",\n" +
                "    \"ConditionDescription\" : \"\"\n" +
                "}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            agreement.ContractID,fieldDescriptorResponse,
            "CreateCondition failed due to empty condition description")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `CreateCondition failed title is empty`(){
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\n" +
                "    \"ConditionTitle\" : \"\",\n" +
                "    \"ConditionDescription\" : \"This is a postman test\"\n" +
                "}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            agreement.ContractID,
            fieldDescriptorResponse,
            "CreateCondition failed due to title being empty")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `CreateCondition failed user is not part of agreement`(){
        //documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\n" +
                "    \"ConditionTitle\" : \"Test\",\n" +
                "    \"ConditionDescription\" : \"This is a postman test\"\n" +
                "}"

        val response = requestSender(rjson,
            userC.publicWalletID,
            agreement.ContractID,
            fieldDescriptorResponse,
            "CreateCondition failed user is not part of agreement")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    fun generateToken(userID: String): String? {
        val signingKey = Keys.hmacShaKeyFor("ThisIsATestKeySpecificallyForTests".toByteArray())
        return Jwts.builder()
            .setSubject(userID)
            .setExpiration(Date(System.currentTimeMillis() + 1080000))
            .signWith(signingKey)
            .compact()
    }
}