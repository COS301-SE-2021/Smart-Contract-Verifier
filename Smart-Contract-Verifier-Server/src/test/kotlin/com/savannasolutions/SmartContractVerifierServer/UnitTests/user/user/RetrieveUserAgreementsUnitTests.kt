package com.savannasolutions.SmartContractVerifierServer.UnitTests.user.user

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveUserAgreementsResponse
import com.savannasolutions.SmartContractVerifierServer.user.services.UserService
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class RetrieveUserAgreementsUnitTests {
    private val userRepository : UserRepository = mock()
    private val agreementsRepository : AgreementsRepository = mock()
    private val conditionsRepository : ConditionsRepository = mock()
    private val userService = UserService(userRepository,agreementsRepository, conditionsRepository)

    private var userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "TestA")

    private fun parameterizedRetrieveUserAgreements(userID: String,
                                                    userExists: Boolean) : RetrieveUserAgreementsResponse
    {
        //Given
        val tempUser = User(userID)
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "TestB")

        var agreement = Agreements(UUID.fromString("6e28cc77-d2e2-4221-abd7-7a6d0e84dbd3"),
            CreatedDate = Date(),
            MovedToBlockChain = true)

        agreement = agreement.apply { users.add(userA) }
        agreement = agreement.apply { users.add(userB)}

        val list : MutableSet<Agreements> = mutableSetOf()
        list.add(agreement)

        userA = userA.apply { agreements = list }

        val agreementList = ArrayList<Agreements>()
        agreementList.add(agreement)

        val userList = ArrayList<User>()
        userList.add(userA)
        userList.add(userB)

        //when
        whenever(userRepository.existsById(userID)).thenReturn(userExists)
        whenever(userRepository.getById(userID)).thenReturn(tempUser)
        whenever(agreementsRepository.getAllByUsersContaining(tempUser)).thenReturn(list)
        whenever(conditionsRepository.getAllByContract(agreement)).thenReturn(agreement.conditions)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(userList)

        //then
        return userService.retrieveUserAgreements(userID)
    }

    @Test
    fun `RetrieveUserAgreements successful without agreement`()
    {
        //Given
        val user = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "TestA")
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(true)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)


        //When
        val response = userService.retrieveUserAgreements(user.publicWalletID)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertEquals(response.Agreements!!, emptyList())
    }

    @Test
    fun `RetrieveUserAgreements successful with an agreement`()
    {

        //When
        val response = parameterizedRetrieveUserAgreements(userA.publicWalletID, true)

        //Then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.Agreements)
        assertTrue { response.Agreements!!.isNotEmpty() }
    }

    @Test
    fun `RetrieveUserAgreements does not exist`()
    {
        //Given

        //When
        val response = parameterizedRetrieveUserAgreements(userA.publicWalletID, false)

        //Then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

}