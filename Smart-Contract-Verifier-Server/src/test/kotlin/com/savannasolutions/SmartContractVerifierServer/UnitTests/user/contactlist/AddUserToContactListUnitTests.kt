package com.savannasolutions.SmartContractVerifierServer.UnitTests.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.AddUserToContactListRequest
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertEquals

internal class AddUserToContactListUnitTests {
    private val userRepository : UserRepository = mock()
    private val contactListRepository : ContactListRepository = mock()
    private val contactListProfileRepository : ContactListProfileRepository = mock()
    private val contactListService = ContactListService(contactListRepository, userRepository, contactListProfileRepository)

    val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "TestA")
    private fun parameterizedAddUserToContactList(userID: String,
                                                    contactListExists: Boolean,
                                                    userAlias: String,
                                                    userExists: Boolean,
                                                  existsByContactAliasAndContactListAndUser: Boolean): ApiResponse<Objects>
    {
        //Given
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        val contactListProfile = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "Test")
        contactListProfile.apply { this.contactList = contactList }
        val nUser = User(userID)
        contactListProfile.apply { this.user = nUser }

        //when
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(userExists)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(nUser)
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(contactListExists)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactListAndUser(contactList, user)).thenReturn(null)
        whenever(contactListProfileRepository.existsByContactAliasAndContactListAndUser(contactListProfile.contactAlias,contactList,user)).thenReturn(existsByContactAliasAndContactListAndUser)
        whenever(contactListProfileRepository.save(any<ContactListProfile>())).thenReturn(contactListProfile)

        //then
        return contactListService.addUserToContactList("owner",
                                                        contactList.contactListID!!,
                                                        AddUserToContactListRequest(userID, userAlias))
    }

    @Test
    fun `AddUserToContactList successful`()
    {
        //given

        //When
        val response = parameterizedAddUserToContactList(user.publicWalletID,
                                                        contactListExists = true,
                                                        "test",
                                                         userExists = true,
                            existsByContactAliasAndContactListAndUser = false)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `AddUserToContactList alias is empty`()
    {
        //Given

        //When
        val response = parameterizedAddUserToContactList(user.publicWalletID,
            contactListExists = true,
            "",
            userExists = true,
        existsByContactAliasAndContactListAndUser = false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `AddUserToContactList userid is empty`()
    {
        //Given

        //When
        val response = parameterizedAddUserToContactList("",
            contactListExists = true,
            "test",
            userExists = true,
        existsByContactAliasAndContactListAndUser = false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `AddUserToContactList userid does not exist in userRepository`()
    {
        //Given

        //When
        val response = parameterizedAddUserToContactList(user.publicWalletID,
            contactListExists = true,
            "test",
            userExists = false,
        existsByContactAliasAndContactListAndUser = false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `AddUserToContactList contactListId does not exist in contactListRepository`()
    {
        //Given

        //When
        val response = parameterizedAddUserToContactList(user.publicWalletID,
            contactListExists = false,
            "test",
            userExists = true,
        existsByContactAliasAndContactListAndUser = false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `AddUserToContactList getAllByContactListAndUser will return a list of length greater than 1`()
    {
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "TestA")
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        val contactListProfile = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "Test")
        contactListProfile.apply { this.contactList = contactList }
        contactListProfile.apply { this.user = user }
        val contactListProfile2 = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "Test")
        contactListProfile2.apply { this.contactList = contactList }
        contactListProfile2.apply { this.user = user }

        val list = ArrayList<ContactListProfile>()
        list.add(contactListProfile)
        list.add(contactListProfile2)

        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(true)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactListAndUser(contactList, user)).thenReturn(list)
        whenever(contactListProfileRepository.existsByContactAliasAndContactListAndUser(contactListProfile.contactAlias,contactList,user)).thenReturn(false)
        whenever(contactListProfileRepository.save(any<ContactListProfile>())).thenReturn(contactListProfile)

        //When
        val response = contactListService.addUserToContactList(
            "other user",
            contactList.contactListID!!,
            AddUserToContactListRequest(user.publicWalletID,
            contactListProfile.contactAlias)
        )

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `AddUserToContactList existsByContactAliasAndContactListAndUser() will return true`()
    {
        //Given

        //When
        val response = parameterizedAddUserToContactList(user.publicWalletID,
            contactListExists = false,
            "test",
            userExists = true,
            existsByContactAliasAndContactListAndUser = true)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }
}