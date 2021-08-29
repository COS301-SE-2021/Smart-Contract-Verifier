package com.savannasolutions.SmartContractVerifierServer.UnitTests.security

import com.savannasolutions.SmartContractVerifierServer.security.configuration.SecurityConfig
import com.savannasolutions.SmartContractVerifierServer.security.services.SecurityService
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.mockito.kotlin.mock

internal class SecurityServiceTest {
    private val userRepository : UserRepository = mock()
    private val securityConfig = SecurityConfig()
    private val securityService = SecurityService(userRepository, securityConfig)

    // TODO: 2021/08/27 ADD TESTS FOR REST OF SECURITY FEATURES. IF HELP IS NEEDED PLEASE CONTACT ME
}