package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.AddUserToContactListRequest
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureDataJpa
class AddUserToContactListDatabaseTest {
    @Autowired
    lateinit var contactListRepository: ContactListRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var contactListProfileRepository: ContactListProfileRepository

    lateinit var contactListService: ContactListService
    lateinit var user : User
    lateinit var contactList : ContactList
    lateinit var ownerUser : User
    lateinit var contactListProfile: ContactListProfile

    @BeforeEach
    fun beforeEach()
    {
        contactListService = ContactListService(contactListRepository,
                                                userRepository,
                                                contactListProfileRepository)

        user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        user = userRepository.save(user)

        ownerUser = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")
        ownerUser = userRepository.save(ownerUser)

        contactList = ContactList(UUID.fromString("f9f858ab-4473-4f72-8b4f-9f3a4aab1a62"),
                                    "Test contact list").apply { this.owner = ownerUser }

        contactList = contactListRepository.save(contactList)
    }

    @AfterEach
    fun afterEach()
    {
        contactListProfileRepository.delete(contactListProfile)
        contactListRepository.delete(contactList)
        userRepository.delete(user)
        userRepository.delete(ownerUser)
    }

    @Test
    fun `AddUserToContactList success`()
    {
        val request = AddUserToContactListRequest(user.publicWalletID,
                                                    contactList.contactListID!!,
                                                    "New user")

        val response = contactListService.addUserToContactList(request)

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        val cLP = contactListProfileRepository.getByContactListAndUser(contactList, user)
        assertNotNull(cLP)
        contactListProfile = cLP
        assertEquals(contactListProfile.contactList, contactList)
        assertEquals(contactListProfile.user, user)
    }
}