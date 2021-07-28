package com.savannasolutions.SmartContractVerifierServer.messenger.repositories

import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageStatusRepository: JpaRepository<MessageStatus, UUID> {
    fun getAllByMessage(messages: Messages): List<MessageStatus>?= emptyList()
}