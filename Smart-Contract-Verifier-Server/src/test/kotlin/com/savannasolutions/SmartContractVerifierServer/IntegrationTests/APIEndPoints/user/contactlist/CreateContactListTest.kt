package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
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
class CreateContactListTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var contactListProfileRepository: ContactListProfileRepository

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var contactListRepository: ContactListRepository

    private lateinit var userA: User
    private lateinit var contactList: ContactList

    @BeforeEach
    private fun beforeEach()
    {
        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        contactList = ContactList(UUID.fromString("e111bc8f-ef5a-46fb-89ab-bff8b4aa4f33"),
                                "TestName")

        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(contactListRepository.existsByOwnerAndContactListName(userA, contactList.contactListName)).thenReturn(false)
        whenever(contactListRepository.existsByOwnerAndContactListName(userA, "broken")).thenReturn(true)
        whenever(contactListRepository.save(any<ContactList>())).thenReturn(contactList)
    }

    private fun requestSender(rjson: String): MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/contactlist/create-contact-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)
        ).andReturn().response
    }

    @Test
    fun `createContractList successful`()
    {
        val rjson = "{\"UserID\" : \"${userA.publicWalletID}\",\"ContactListName\" : \"TestName\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, contactList.contactListID!!.toString())
    }

    @Test
    fun `createContractList failed due to contact list name being empty`()
    {
        val rjson = "{\"UserID\" : \"${userA.publicWalletID}\",\"ContactListName\" : \"\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `createContractList failed due to user being empty`()
    {
        val rjson = "{\"UserID\" : \"\",\"ContactListName\" : \"TestName\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `createContractList failed due to user not existing`()
    {
        val rjson = "{\"UserID\" : \"random user\",\"ContactListName\" : \"TestName\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `createContractList failed due to a contact list already existing with name and user`()
    {
        val rjson = "{\"UserID\" : \"${userA.publicWalletID}\",\"ContactListName\" : \"broken\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }
}