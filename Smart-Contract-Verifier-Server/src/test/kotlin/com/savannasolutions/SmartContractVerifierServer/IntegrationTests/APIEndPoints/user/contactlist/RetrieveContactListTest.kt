package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveContactListResponse
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
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "docs/api/get/user/userID/contactList/contactListID")
class RetrieveContactListTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var contactListProfileRepository: ContactListProfileRepository

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var contactListRepository: ContactListRepository

    private lateinit var contactList: ContactList
    private lateinit var userA: User
    private lateinit var userB: User
    private lateinit var userC: User


    @BeforeEach
    fun beforeEach()
    {
        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")
        userC = User("0x37Ec9a77BFa094b24054422564e68B08aF3114B4")

        contactList = ContactList(
            UUID.fromString("e111bc8f-ef5a-46fb-89ab-bff8b4aa4f33"),
            "TestName")
        contactList.owner = userA

        val contactListProfileA = ContactListProfile(UUID.fromString("e529abd9-5a84-468d-a1f4-68c64af29b2b"),
                                                        "AliasA")
        contactListProfileA.user = userB
        contactListProfileA.contactList = contactList
        val contactListProfileB = ContactListProfile(UUID.fromString("d82611e1-e1bf-45d6-bc3c-c928b6a9ecc8"),
                                                        "AliasB")

        val contactListProfileList = ArrayList<ContactListProfile>()
        contactListProfileList.add(contactListProfileA)
        contactListProfileList.add(contactListProfileB)

        contactListProfileB.user = userC
        contactListProfileB.contactList = contactList
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(true)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactList(contactList)).thenReturn(contactListProfileList)
    }

    private fun requestSender(userID: String,
                              contactListID: UUID,
                              responseFieldDescriptors: ArrayList<FieldDescriptor>,
                              testName: String): MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders.get("/user/${userID}/contactList/${contactListID}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
            MockMvcRestDocumentation.document(
                testName,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.responseFields(responseFieldDescriptors)
            )
        ).andReturn().response
    }

    @Test
    fun `RetrieveContactList successful`(){
        //Documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiResponse())
        fieldDescriptorResponse.addAll(RetrieveContactListResponse.response())
        //End of documentation

        val response = requestSender(userA.publicWalletID,
            contactList.contactListID!!,
            fieldDescriptorResponse,
            "RetrieveContactList successful")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, userB.publicWalletID)
        assertContains(response.contentAsString, userC.publicWalletID)
    }

    @Test
    fun `RetrieveContactList failed due to contact list not existing`(){
        //Documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val response = requestSender(userA.publicWalletID,
            UUID.fromString("eb558bea-389e-4e7b-afed-4987dbf37f85"),
            fieldDescriptorResponse,
            "RetrieveContactList failed due to contact list not existing")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }


}