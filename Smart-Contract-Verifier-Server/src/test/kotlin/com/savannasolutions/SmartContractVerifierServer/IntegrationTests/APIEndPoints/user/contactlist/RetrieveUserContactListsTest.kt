package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.APIEndPoints.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
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
class RetrieveUserContactListsTest {
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
    private lateinit var contactListA: ContactList
    private lateinit var contactListB: ContactList

    @BeforeEach
    fun beforeEach()
    {
        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")

        contactListA = ContactList(UUID.fromString("57d1648d-98a1-4d88-93e9-47afaf88d9a5"), "help")
        contactListB = ContactList(UUID.fromString("18dc472f-46b1-4363-9556-82b20f718c0d"), "help")
        val contactListList = ArrayList<ContactList>()
        contactListList.add(contactListA)
        contactListList.add(contactListB)
        contactListA.owner = userA
        contactListB.owner = userA
        userA.contactList = contactListList

        whenever(userRepository.existsById(userA.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userA.publicWalletID)).thenReturn(userA)
        whenever(userRepository.existsById(userB.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(userB.publicWalletID)).thenReturn(userB)
        whenever(contactListRepository.getAllByOwner(userA)).thenReturn(contactListList)
    }

    private fun requestSender(rjson: String): MockHttpServletResponse {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/contactlist/retrieve-user-contact-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rjson)
        ).andReturn().response
    }

    @Test
    fun `RetrieveUserContactLists successful`()
    {
        val rjson = "{\"UserID\" : \"${userA.publicWalletID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, contactListA.contactListID!!.toString())
        assertContains(response.contentAsString, contactListB.contactListID!!.toString())
    }

    @Test
    fun `RetrieveUserContactLists failed user id is empty`()
    {
        val rjson = "{\"UserID\" : \"\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `RetrieveUserContactLists failed user does not exist`()
    {
        val rjson = "{\"UserID\" : \"failed user\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"FAILED\"")
    }

    @Test
    fun `RetrieveUserContactLists successful but list is empty`()
    {
        val rjson = "{\"UserID\" : \"${userB.publicWalletID}\"}"

        val response = requestSender(rjson)

        assertContains(response.contentAsString, "\"Status\":\"SUCCESSFUL\"")
        assertContains(response.contentAsString, "ListInfo\":[]")
    }

}