package com.savannasolutions.SmartContractVerifierServer.UnitTests.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.responses.CreateContactListResponse
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertEquals

internal class CreateContactListUnitTests {
    private val userRepository : UserRepository = mock()
    private val contactListRepository : ContactListRepository = mock()
    private val contactListProfileRepository : ContactListProfileRepository = mock()
    private val contactListService = ContactListService(contactListRepository, userRepository, contactListProfileRepository)

    private val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "TestA")

    private fun parameterizedCreateContactList(userID: String,
                                                contactListName: String,
                                                userExists: Boolean,
                                                existsByOwnerAndContactListName: Boolean): ApiResponse<CreateContactListResponse>
    {
        //Given
        val tUser = User(userID)
        var contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), contactListName)
        contactList = contactList.apply { owner = user }

        //when
        whenever(userRepository.existsById(tUser.publicWalletID)).thenReturn(userExists)
        whenever(userRepository.getById(tUser.publicWalletID)).thenReturn(tUser)
        whenever(contactListRepository.existsByOwnerAndContactListName(tUser,contactList.contactListName)).thenReturn(existsByOwnerAndContactListName)
        whenever(contactListRepository.save(any<ContactList>())).thenReturn(contactList)

        //then
        return contactListService.createContactList(userID, contactListName)
    }

    @Test
    fun `CreateContactList successful`(){
        //Given

        //When
        val response = parameterizedCreateContactList(user.publicWalletID,
                                                      "testContactList",
                                                        userExists = true,
                                                        existsByOwnerAndContactListName = false)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `CreateContactList contact list name is empty`(){
        //Given

        //When
        val response = parameterizedCreateContactList(user.publicWalletID,
            "",
            userExists = true,
            existsByOwnerAndContactListName = false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `CreateContactList user does not exist`(){
        //Given

        //When
        val response = parameterizedCreateContactList(user.publicWalletID,
            "testContactList",
            userExists = false,
            existsByOwnerAndContactListName = false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `CreateContactList exists by owner and contact list name returns true`(){
        //Given

        //When
        val response = parameterizedCreateContactList(user.publicWalletID,
            "testContactList",
            userExists = true,
            existsByOwnerAndContactListName = true)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

}