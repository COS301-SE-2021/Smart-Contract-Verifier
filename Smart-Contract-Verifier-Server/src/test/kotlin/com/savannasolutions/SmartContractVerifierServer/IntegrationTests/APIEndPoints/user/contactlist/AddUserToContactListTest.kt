package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
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
@AutoConfigureRestDocs(outputDir = "docs/api/put/user/userID/contactList/contactListID")
class AddUserToContactListTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var contactListProfileRepository: ContactListProfileRepository

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var contactListRepository: ContactListRepository

    private lateinit var userA: User
    private lateinit var userB: User
    private lateinit var userC: User
    private lateinit var contactList: ContactList
    private lateinit var wrongContactList : ContactList

    @BeforeEach
    fun beforeEach()
    {
        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")
        userC = User("0x37Ec9a77BFa094b24054422564e68B08aF3114B4")

        contactList = ContactList(UUID.fromString("33e660b7-8522-416b-9b8d-523deea5a778"),
                                    "Test Name")

        wrongContactList = ContactList(UUID.fromString("9fd1c237-0f1c-4e40-9bb4-3e8d16969a07"),
                                        "Wrong name")

        contactList.owner = userA
        wrongContactList.owner = userA
        val contactListProfile = ContactListProfile(UUID.fromString("1998f360-eefb-44c3-91b8-3b744d6724ac"),
                                                    "TestAlias")
        contactListProfile.user = userC
        contactListProfile.contactList = contactList

        val contactListProfileList = ArrayList<ContactListProfile>()
        contactListProfileList.add(contactListProfile)

        val wrongContactListList = ArrayList<ContactListProfile>()
        wrongContactListList.add(contactListProfile)
        wrongContactListList.add(contactListProfile)

        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(true)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListRepository.existsById(wrongContactList.contactListID!!)).thenReturn(true)
        whenever(contactListRepository.getById(wrongContactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactListAndUser(contactList, userA)).thenReturn(contactListProfileList)
        whenever(contactListProfileRepository.existsByContactAliasAndContactListAndUser(contactListProfile.contactAlias,
                                                                                            contactList, contactListProfile.user)).thenReturn(true)
        whenever(contactListProfileRepository.existsByContactAliasAndContactListAndUser(contactListProfile.contactAlias,
            contactList, userB)).thenReturn(false)
        whenever(contactListProfileRepository.getAllByContactListAndUser(wrongContactList, userA)).thenReturn(wrongContactListList)
        whenever(contactListProfileRepository.save(any<ContactListProfile>())).thenReturn(contactListProfile)
    }

    private fun requestSender(rjson: String,
                              userID: String,
                              contactListID: UUID,
                              responseFieldDescriptors: ArrayList<FieldDescriptor>,
                              testName: String): MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders.put("/user/${userID}/contactList/${contactListID}")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "bearer ${generateToken(userID)}")
                .content(rjson)
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
    fun `AddUserToContactList successful`()
    {
        //Documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiEmptyResponse())
        //End of documentation

        val rjson = "{\"NewUserID\" : \"${userB.publicWalletID}\"," +
                "    \"NewUserAlias\" : \"TestAlias\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            contactList.contactListID!!,
            fieldDescriptorResponse,
            "AddUserToContactList successful")

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `AddUserToContactList failed due to user alias being empty`()
    {
        //Documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation


        val rjson = "{\"NewUserID\" : \"${userB.publicWalletID}\"," +
                "    \"NewUserAlias\" : \"\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            contactList.contactListID!!,
            fieldDescriptorResponse,
            "AddUserToContactList failed due to user alias being empty")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `AddUserToContactList failed due to NewUserID being empty`()
    {
        //Documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"NewUserID\" : \"\"," +
                "    \"NewUserAlias\" : \"TestAlias\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            contactList.contactListID!!,
            fieldDescriptorResponse,
            "AddUserToContactList failed due to NewUserID being empty")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `AddUserToContactList failed due to user not existing`()
    {
        //Documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"NewUserID\" : \"not correct user\"," +
                "    \"NewUserAlias\" : \"TestAlias\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            contactList.contactListID!!,
            fieldDescriptorResponse,
            "AddUserToContactList failed due to user not existing")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `AddUserToContactList failed contact list does not exist`()
    {
        //Documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"NewUserID\" : \"${userB.publicWalletID}\"," +
                "    \"NewUserAlias\" : \"TestAlias\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            UUID.fromString("310deb0e-e828-44c9-b9c0-ca35b4142913"),
            fieldDescriptorResponse,
            "AddUserToContactList failed contact list does not exist")

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `AddUserToContactList failed user trying to add himself`()
    {
        //Documentation
        val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
        fieldDescriptorResponse.addAll(ApiResponse.apiFailedResponse())
        //End of documentation

        val rjson = "{\"NewUserID\" : \"${userA.publicWalletID}\"," +
                "    \"NewUserAlias\" : \"TestAlias\"}"

        val response = requestSender(rjson,
            userA.publicWalletID,
            contactList.contactListID!!,
            fieldDescriptorResponse,
            "AddUserToContactList failed user trying to add himself")

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