package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.user.repositories.ContactListRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ContactListServiceTest {
    private val userRepository : UserRepository = mock()
    private val contactListRepository : ContactListRepository = mock()
    private val conditionListService = ContactListService(contactListRepository, userRepository)
}