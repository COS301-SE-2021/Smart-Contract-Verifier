package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureDataJpa
class CreateContactListDatabaseTest {
    @Autowired
    lateinit var contactListRepository: ContactListRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var contactListProfileRepository: ContactListProfileRepository

    lateinit var contactListService: ContactListService
    lateinit var user : User
    lateinit var contactList : ContactList

    @BeforeEach
    fun beforeEach()
    {
        contactListService = ContactListService(contactListRepository,
            userRepository,
            contactListProfileRepository)

        user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        user = userRepository.save(user)
    }

    @AfterEach
    fun afterEach()
    {
        contactListRepository.delete(contactList)
        userRepository.delete(user)
    }

    @Test
    fun `CreateContactList successful`()
    {
        val response = contactListService.createContactList(user.publicWalletID, "test")

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.ContactListID)
        contactList = contactListRepository.getById(response.ContactListID!!)
        assertEquals(contactList.contactListName, "test")
        assertEquals(contactList.owner, user)
    }
}