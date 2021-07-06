package com.savannasolutions.SmartContractVerifierServer.user.repositories

import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ContactListRepository: JpaRepository<ContactList, UUID>{
    fun existsByOwnerAndContactListName(owner: User, contactListName:String):Boolean
}