package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
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
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureDataJpa
class RemoveUserFromContactListDatabaseTest {
    @Autowired
    lateinit var contactListRepository: ContactListRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var contactListProfileRepository: ContactListProfileRepository

    lateinit var contactListService: ContactListService
    lateinit var otherUser : User
    lateinit var testContactList : ContactList
    lateinit var ownerUser : User
    lateinit var contactListProfile: ContactListProfile

    @BeforeEach
    fun beforeEach()
    {
        contactListService = ContactListService(contactListRepository,
            userRepository,
            contactListProfileRepository)

        otherUser = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        otherUser = userRepository.save(otherUser)

        ownerUser = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")
        ownerUser = userRepository.save(ownerUser)

        testContactList = ContactList(
            UUID.fromString("f9f858ab-4473-4f72-8b4f-9f3a4aab1a62"),
            "Test contact list").apply { this.owner = ownerUser }

        testContactList = contactListRepository.save(testContactList)

        contactListProfile = ContactListProfile(UUID.fromString("7bd871eb-a9a3-4409-914c-2a184dd5d89e"),
                                                "Test profile").apply { user = otherUser }.apply { contactList = testContactList }
        contactListProfile = contactListProfileRepository.save(contactListProfile)
    }

    @AfterEach
    fun afterEach()
    {
        contactListRepository.delete(testContactList)
        userRepository.delete(ownerUser)
        userRepository.delete(otherUser)
    }

    @Test
    fun `RemoveUserFromContactList success`()
    {
        val response = contactListService.removeUserFromContactList(ownerUser.publicWalletID,
                                                                    testContactList.contactListID!!,
                                                                     otherUser.publicWalletID)

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

}