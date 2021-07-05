package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserServiceTest {
    private val userRepository : UserRepository = mock()
    private val agreementsRepository : AgreementsRepository = mock()
    private val userService = UserService(userRepository,agreementsRepository)
}