package com.savannasolutions.SmartContractVerifierServer.user.repositories

import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactListProfile
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ContactListProfileRepository: JpaRepository<ContactListProfile, UUID> {
    fun getAllByContactListAndUser(contactList: ContactList, user: User): List<ContactListProfile>?

    fun existsByContactAliasAndContactListAndUser(contactAlias: String, contactList: ContactList, user: User): Boolean

    fun getByContactListAndUser(contactList: ContactList, user: User): ContactListProfile?

    fun existsByContactListAndUser(contactList: ContactList, user: User): Boolean

    fun getAllByContactList(contactList: ContactList): List<ContactListProfile>
}