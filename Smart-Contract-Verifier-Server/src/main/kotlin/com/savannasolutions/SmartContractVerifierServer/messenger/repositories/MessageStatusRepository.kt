package com.savannasolutions.SmartContractVerifierServer.messenger.repositories

import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageStatusRepository: JpaRepository<MessageStatus, UUID> {
    fun getAllByMessage(messages: Messages): List<MessageStatus>?= emptyList()
    fun getAllByRecipient(recipient : User): List<MessageStatus>?= emptyList()
    fun getByRecipientAndMessage(recipient: User, messages: Messages): MessageStatus?
}