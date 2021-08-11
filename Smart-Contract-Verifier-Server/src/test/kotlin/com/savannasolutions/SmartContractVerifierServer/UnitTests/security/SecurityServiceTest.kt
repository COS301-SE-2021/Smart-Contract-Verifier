package com.savannasolutions.SmartContractVerifierServer.UnitTests.security

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.security.requests.UserExistsRequest
import com.savannasolutions.SmartContractVerifierServer.security.services.SecurityService
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class SecurityServiceTest {
    private val userRepository : UserRepository = mock()
    private val securityService = SecurityService(userRepository)

    @Test
    fun `UserExists successful and true`()
    {
        //given
        whenever(userRepository.existsById("walletAddress")).thenReturn(true)

        //when
        val response = securityService.userExists(UserExistsRequest("walletAddress"))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertTrue { response.Exists }
    }

    @Test
    fun `UserExists successful and false`()
    {
        //given
        whenever(userRepository.existsById("walletAddress")).thenReturn(false)

        //when
        val response = securityService.userExists(UserExistsRequest("walletAddress"))

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertFalse { response.Exists }
    }

    @Test
    fun `UserExists failed due to empty wallet id`()
    {
        //given

        //when
        val response = securityService.userExists(UserExistsRequest(""))

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }
}