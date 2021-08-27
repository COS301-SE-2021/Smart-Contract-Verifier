package com.savannasolutions.SmartContractVerifierServer.UnitTests.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertEquals

internal class RemoveUserFromContactListUnitTests {
    private val userRepository : UserRepository = mock()
    private val contactListRepository : ContactListRepository = mock()
    private val contactListProfileRepository : ContactListProfileRepository = mock()
    private val contactListService = ContactListService(contactListRepository, userRepository, contactListProfileRepository)

    private val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "TestA")

    private fun parameterizedRemoveUserFromContactList(userID: String,
                                                        userExists: Boolean,
                                                        contactListExists: Boolean,
                                                        existsByContactListAndUser: Boolean,
                                                        contactListProfileExists: Boolean): RemoveUserFromContactListResponse
    {
        //Given
        val tUser = User(userID)
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        val contactListProfile = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "Test")
        contactListProfile.apply { this.contactList = contactList }
        contactListProfile.apply { this.user = tUser }

        //when
        whenever(userRepository.existsById("other")).thenReturn(true)
        whenever(userRepository.getById("other")).thenReturn(User("other"))
        whenever(userRepository.existsById(userID)).thenReturn(userExists)
        whenever(userRepository.getById(userID)).thenReturn(tUser)
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(contactListExists)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.existsByContactListAndUser(contactList,tUser)).thenReturn(existsByContactListAndUser)
        whenever(contactListProfileRepository.getByContactListAndUser(contactList,tUser)).thenReturn(contactListProfile)
        whenever(contactListProfileRepository.existsById(contactListProfile.ProfileID!!)).thenReturn(contactListProfileExists)

        //then
        return contactListService.removeUserFromContactList("other",contactList.contactListID!!,userID)
    }

    @Test
    fun `RemoveUserFromContactList successful`()
    {
        //given

        //When
        val response = parameterizedRemoveUserFromContactList(user.publicWalletID,
                                                                userExists = true,
                                                                contactListExists = true,
                                                                existsByContactListAndUser = true,
                                                                contactListProfileExists = false)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `RemoveUserFromContactList userid is empty`()
    {
        //Given

        //When
        val response = parameterizedRemoveUserFromContactList("",
            userExists = true,
            contactListExists = true,
            existsByContactListAndUser = true,
            contactListProfileExists = false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RemoveUserFromContactList userid does not exist`()
    {
        //Given

        //When
        val response = parameterizedRemoveUserFromContactList(user.publicWalletID,
            userExists = false,
            contactListExists = true,
            existsByContactListAndUser = true,
            contactListProfileExists = false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RemoveUserFromContactList contact list does not exist`()
    {
        //Given

        //When
        val response = parameterizedRemoveUserFromContactList(user.publicWalletID,
            userExists = true,
            contactListExists = false,
            existsByContactListAndUser = true,
            contactListProfileExists = false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RemoveUserFromContactList exitsByContactListAndUser will return false`()
    {
        //Given

        //When
        val response = parameterizedRemoveUserFromContactList(user.publicWalletID,
            userExists = true,
            contactListExists = true,
            existsByContactListAndUser = false,
            contactListProfileExists = false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RemoveUserFromContactList failed to delete from database, exits returns true`()
    {
        //Given

        //When
        val response = parameterizedRemoveUserFromContactList(user.publicWalletID,
            userExists = true,
            contactListExists = true,
            existsByContactListAndUser = true,
            contactListProfileExists = true)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

}