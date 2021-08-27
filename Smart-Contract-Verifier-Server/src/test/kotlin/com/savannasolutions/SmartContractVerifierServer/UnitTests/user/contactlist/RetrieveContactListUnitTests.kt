package com.savannasolutions.SmartContractVerifierServer.UnitTests.user.contactlist

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveContactListResponse
import com.savannasolutions.SmartContractVerifierServer.user.services.ContactListService
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class RetrieveContactListUnitTests {
    private val userRepository : UserRepository = mock()
    private val contactListRepository : ContactListRepository = mock()
    private val contactListProfileRepository : ContactListProfileRepository = mock()
    private val contactListService = ContactListService(contactListRepository, userRepository, contactListProfileRepository)

    private val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "TestA")

    private fun parameterizedRetrieveContactList(userID: String,
                                                    contactListExists: Boolean,
                                                    ): RetrieveContactListResponse
    {
        //Given
        val tempUser = User(userID)
        var contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        var contactListProfile = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "Test")
        contactListProfile = contactListProfile.apply { this.contactList = contactList }
        contactListProfile = contactListProfile.apply { this.user = User("other user") }
        val list = ArrayList<ContactListProfile>()
        list.add(contactListProfile)
        contactList = contactList.apply { this.contactListProfiles = list}.apply { owner = tempUser }

        //When
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(contactListExists)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactList(contactList)).thenReturn(contactList.contactListProfiles)

        //Then
        return contactListService.retrieveContactList(userID, contactList.contactListID!!)

    }

    @Test
    fun `RetrieveContactList successful with empty list`()
    {
        //Given
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName").apply { owner = user }

        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(true)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)


        //When
        val response = contactListService.retrieveContactList(user.publicWalletID, contactList.contactListID!!)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertEquals(response.WalletAndAlias, emptyList())
    }

    @Test
    fun `RetrieveContactList successful with populated list`()
    {
        //Given

        //When
        val response = parameterizedRetrieveContactList(user.publicWalletID,
                                                        true)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.WalletAndAlias)
        assertTrue { response.WalletAndAlias!!.isNotEmpty() }
    }

    @Test
    fun `RetrieveContactList contact list does not exist`()
    {
        //Given

        //When
        val response = parameterizedRetrieveContactList(user.publicWalletID,
            false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }
}