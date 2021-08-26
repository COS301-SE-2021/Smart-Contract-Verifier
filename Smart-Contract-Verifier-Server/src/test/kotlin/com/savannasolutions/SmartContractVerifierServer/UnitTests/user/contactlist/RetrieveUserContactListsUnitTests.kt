package com.savannasolutions.SmartContractVerifierServer.UnitTests.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveUserContactListResponse
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class RetrieveUserContactListRequest {
    private val userRepository : UserRepository = mock()
    private val contactListRepository : ContactListRepository = mock()
    private val contactListProfileRepository : ContactListProfileRepository = mock()
    private val contactListService = ContactListService(contactListRepository, userRepository, contactListProfileRepository)

    private val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "TestA")

    private fun parameterizedRetrieveUserContactList(userID: String,
                                                    userExists: Boolean): RetrieveUserContactListResponse
    {
        //Given
        var tempUser = User(userID)
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")

        val list = ArrayList<ContactList>()
        list.add(contactList)

        tempUser = tempUser.apply { this.contactList = list }

        //When
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(userExists)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(tempUser)
        whenever(contactListRepository.getAllByOwner(user)).thenReturn(list)

        //Then
        return contactListService.retrieveUserContactLists(userID)
    }

    @Test
    fun `RetrieveUserContactLists successful empty list`()
    {
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "TestA")

        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)

        //When
        val response = contactListService.retrieveUserContactLists(user.publicWalletID)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertEquals(response.ContactListInfo, emptyList())
    }

    @Test
    fun `RetrieveUserContactLists successful with populated list`()
    {
        //Given

        //When
        val response = parameterizedRetrieveUserContactList(user.publicWalletID, true)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.ContactListInfo)
        assertTrue { response.ContactListInfo!!.isNotEmpty() }
    }

    @Test
    fun `RetrieveUserContactLists user id is empty`()
    {
        //Given

        //When
        val response = parameterizedRetrieveUserContactList("", true)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RetrieveUserContactLists userid does not exist`()
    {
        //Given

        //When
        val response = parameterizedRetrieveUserContactList(user.publicWalletID, false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }
}