package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.security

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.security.configuration.SecurityConfig
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.security.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.security.services.SecurityService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureDataJpa
class AddUserDatabaseTest {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var securityConfig: SecurityConfig

    lateinit var securityService: SecurityService

    lateinit var user : User

    @BeforeEach
    fun beforeEach()
    {
        securityService = SecurityService(userRepository,
                                            securityConfig)
        user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
    }

    @AfterEach
    fun afterEach()
    {
        userRepository.delete(user)
    }

    @Test
    fun `AddUser successful`()
    {
        val request = AddUserRequest(user.publicWalletID, "test")

        val response = securityService.addUser(request)

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(userRepository.getById(user.publicWalletID))
    }
}