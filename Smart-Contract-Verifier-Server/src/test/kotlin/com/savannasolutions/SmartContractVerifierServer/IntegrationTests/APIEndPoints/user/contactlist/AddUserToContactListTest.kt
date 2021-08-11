package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.util.*
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
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

    private fun requestSender(rjson: String): MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/contactlist/add-user-to-contact-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)
        ).andReturn().response
    }

    @Test
    fun `AddUserToContactList successful`()
    {
        val rjson = "{\"NewUserID\" : \"${userB.publicWalletID}\",\"ContactListID\" : \"${contactList.contactListID}\"," +
                "    \"NewUserAlias\" : \"TestAlias\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
    }

    @Test
    fun `AddUserToContactList failed due to user alias being empty`()
    {
        val rjson = "{\"NewUserID\" : \"${userB.publicWalletID}\",\"ContactListID\" : \"${contactList.contactListID}\"," +
                "    \"NewUserAlias\" : \"\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `AddUserToContactList failed due to NewUserID being empty`()
    {
        val rjson = "{\"NewUserID\" : \"\",\"ContactListID\" : \"${contactList.contactListID}\"," +
                "    \"NewUserAlias\" : \"TestAlias\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `AddUserToContactList failed due to user not exisiting`()
    {
        val rjson = "{\"NewUserID\" : \"not correct user\",\"ContactListID\" : \"${contactList.contactListID}\"," +
                "    \"NewUserAlias\" : \"TestAlias\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `AddUserToContactList failed contact list does not exist`()
    {
        val rjson = "{\"NewUserID\" : \"${userB.publicWalletID}\",\"ContactListID\" : \""+UUID.fromString("310deb0e-e828-44c9-b9c0-ca35b4142913")+"\"," +
                "    \"NewUserAlias\" : \"TestAlias\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
}