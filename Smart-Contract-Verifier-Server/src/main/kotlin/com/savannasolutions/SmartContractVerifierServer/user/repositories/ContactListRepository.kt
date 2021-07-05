package com.savannasolutions.SmartContractVerifierServer.user.repositories

import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ContactListRepository: JpaRepository<ContactList, UUID>