package com.savannasolutions.SmartContractVerifierServer.messenger.repositories

import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessagesRepository: JpaRepository<Messages, UUID> {
    fun getAllBySender(user: User): List<Messages>?
    fun getAllByAgreements(agreements: Agreements): List<Messages>?
}