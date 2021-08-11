package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
import kotlin.collections.ArrayList
import kotlin.test.assertContains

@SpringBootTest
@AutoConfigureMockMvc
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

    private fun requestSender(rjson: String): MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/contactlist/retrieve-contact-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)
        ).andReturn().response
    }

    @Test
    fun `RetrieveContactList successful`(){
        val rjson = "{\"ContactListID\" : \"${contactList.contactListID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, userB.publicWalletID)
        assertContains(response.contentAsString, userC.publicWalletID)
    }

    @Test
    fun `RetrieveContactList failed due to contact list not existing`(){
        val rjson = "{\"ContactListID\" : \"d3de557a-08a1-47d3-8769-eaa3fbb11220\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }


}