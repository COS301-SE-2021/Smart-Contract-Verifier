package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListProfileRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.AddUserToContactListRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.CreateContactListRequest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals

class ContactListServiceTest {
    private val userRepository : UserRepository = mock()
    private val contactListRepository : ContactListRepository = mock()
    private val contactListProfileRepository : ContactListProfileRepository = mock()
    private val contactListService = ContactListService(contactListRepository, userRepository, contactListProfileRepository)

    @Test
    fun `AddUserToContactList successful`()
    {
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        val contactListProfile = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "Test")
        contactListProfile.apply { this.contactList = contactList }
        contactListProfile.apply { this.user = user }


        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(true)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactListAndUser(contactList, user)).thenReturn(null)
        whenever(contactListProfileRepository.existByAliasAndContactListAndUser(contactList,user,contactListProfile.ContactAlias)).thenReturn(false)
        whenever(contactListProfileRepository.save(any<ContactListProfile>())).thenReturn(contactListProfile)

        //When
        val response = contactListService.addUserToContactList(AddUserToContactListRequest(user.publicWalletID,
                                                                                                contactList.contactListID!!,
                                                                                                contactListProfile.ContactAlias))

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `AddUserToContactList alias is empty`()
    {
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","testA")
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        val contactListProfile = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "")
        contactListProfile.apply { this.contactList = contactList }
        contactListProfile.apply { this.user = user }


        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(true)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactListAndUser(contactList, user)).thenReturn(null)
        whenever(contactListProfileRepository.existByAliasAndContactListAndUser(contactList,user,contactListProfile.ContactAlias)).thenReturn(false)
        whenever(contactListProfileRepository.save(any<ContactListProfile>())).thenReturn(contactListProfile)

        //When
        val response = contactListService.addUserToContactList(AddUserToContactListRequest(user.publicWalletID,
                contactList.contactListID!!,
                contactListProfile.ContactAlias))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `AddUserToContactList userid is empty`()
    {
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        val contactListProfile = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "Test")
        contactListProfile.apply { this.contactList = contactList }
        contactListProfile.apply { this.user = user }


        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(true)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactListAndUser(contactList, user)).thenReturn(null)
        whenever(contactListProfileRepository.existByAliasAndContactListAndUser(contactList,user,contactListProfile.ContactAlias)).thenReturn(false)
        whenever(contactListProfileRepository.save(any<ContactListProfile>())).thenReturn(contactListProfile)

        //When
        val response = contactListService.addUserToContactList(AddUserToContactListRequest("",
                contactList.contactListID!!,
                contactListProfile.ContactAlias))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `AddUserToContactList userid does not exist in userRepository`()
    {
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        val contactListProfile = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "Test")
        contactListProfile.apply { this.contactList = contactList }
        contactListProfile.apply { this.user = user }


        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(false)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(true)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactListAndUser(contactList, user)).thenReturn(null)
        whenever(contactListProfileRepository.existByAliasAndContactListAndUser(contactList,user,contactListProfile.ContactAlias)).thenReturn(false)
        whenever(contactListProfileRepository.save(any<ContactListProfile>())).thenReturn(contactListProfile)

        //When
        val response = contactListService.addUserToContactList(AddUserToContactListRequest(user.publicWalletID,
                contactList.contactListID!!,
                contactListProfile.ContactAlias))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `AddUserToContactList contactListId does not exist in contactListRepository`()
    {
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        val contactListProfile = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "Test")
        contactListProfile.apply { this.contactList = contactList }
        contactListProfile.apply { this.user = user }


        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(false)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactListAndUser(contactList, user)).thenReturn(null)
        whenever(contactListProfileRepository.existByAliasAndContactListAndUser(contactList,user,contactListProfile.ContactAlias)).thenReturn(false)
        whenever(contactListProfileRepository.save(any<ContactListProfile>())).thenReturn(contactListProfile)

        //When
        val response = contactListService.addUserToContactList(AddUserToContactListRequest(user.publicWalletID,
                contactList.contactListID!!,
                contactListProfile.ContactAlias))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `AddUserToContactList getAllByContactListAndUser will return a list of length greater than 1`()
    {
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
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
        whenever(contactListProfileRepository.existByAliasAndContactListAndUser(contactList,user,contactListProfile.ContactAlias)).thenReturn(false)
        whenever(contactListProfileRepository.save(any<ContactListProfile>())).thenReturn(contactListProfile)

        //When
        val response = contactListService.addUserToContactList(AddUserToContactListRequest(user.publicWalletID,
                contactList.contactListID!!,
                contactListProfile.ContactAlias))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `AddUserToContactList existByAliasAndContactListAndUser will return true`()
    {
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
        val contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        val contactListProfile = ContactListProfile(UUID.fromString("8a4904c5-0ffb-4567-88de-233b160d7ddc"), "Test")
        contactListProfile.apply { this.contactList = contactList }
        contactListProfile.apply { this.user = user }


        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsById(contactList.contactListID!!)).thenReturn(true)
        whenever(contactListRepository.getById(contactList.contactListID!!)).thenReturn(contactList)
        whenever(contactListProfileRepository.getAllByContactListAndUser(contactList, user)).thenReturn(null)
        whenever(contactListProfileRepository.existByAliasAndContactListAndUser(contactList,user,contactListProfile.ContactAlias)).thenReturn(true)
        whenever(contactListProfileRepository.save(any<ContactListProfile>())).thenReturn(contactListProfile)

        //When
        val response = contactListService.addUserToContactList(AddUserToContactListRequest(user.publicWalletID,
                contactList.contactListID!!,
                contactListProfile.ContactAlias))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `CreateContactList successful`(){
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
        var contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        contactList = contactList.apply { owner = user }

        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsByOwnerAndContactListName(user,contactList.contactListName)).thenReturn(false)
        whenever(contactListRepository.save(any<ContactList>())).thenReturn(contactList)


        //When
        val response = contactListService.createContactList(CreateContactListRequest(user.publicWalletID,contactList.contactListName))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `CreateContactList contact list name is empty`(){
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
        var contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        contactList = contactList.apply { owner = user }

        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsByOwnerAndContactListName(user,contactList.contactListName)).thenReturn(false)


        //When
        val response = contactListService.createContactList(CreateContactListRequest(user.publicWalletID,""))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `CreateContactList user id is empty`(){
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
        var contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")

        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsByOwnerAndContactListName(user,contactList.contactListName)).thenReturn(false)


        //When
        val response = contactListService.createContactList(CreateContactListRequest("",contactList.contactListName))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `CreateContactList user does not exist`(){
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
        var contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")
        contactList = contactList.apply { owner = user }

        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(false)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsByOwnerAndContactListName(user,contactList.contactListName)).thenReturn(false)
        whenever(contactListRepository.save(any<ContactList>())).thenReturn(contactList)


        //When
        val response = contactListService.createContactList(CreateContactListRequest(user.publicWalletID,contactList.contactListName))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `CreateContactList exists by owner and contact list name returns true`(){
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "test@test.com","TestA")
        var contactList = ContactList(UUID.fromString("2b4dc93a-92f5-4425-9a11-073ce06d14c7"), "TestName")

        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(contactListRepository.existsByOwnerAndContactListName(user,contactList.contactListName)).thenReturn(true)


        //When
        val response = contactListService.createContactList(CreateContactListRequest(user.publicWalletID,contactList.contactListName))

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

}