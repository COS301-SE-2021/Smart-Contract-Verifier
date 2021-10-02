package com.savannasolutions.SmartContractVerifierServer.UnitTests.stats

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.models.Judges
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.stats.responses.DetailedStatsResponse
import com.savannasolutions.SmartContractVerifierServer.stats.services.StatsService
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.ZoneId
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DetailedStatsUnitTests {
/*    var agreementsRepository : AgreementsRepository = mock()
    var userRepository : UserRepository = mock()
    var judgesRepository: JudgesRepository = mock()

    private val statsService = StatsService(agreementsRepository = agreementsRepository,
        userRepository = userRepository,
        judgesRepository = judgesRepository)

    private fun parameterizedDetailedStats(startDate: Date, endDate: Date, createdDate: Date, sealedDate: Date): ApiResponse<DetailedStatsResponse>
    {
        //given
        val userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "testA")
        val userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "testB")
        var mockAgreementBC = Agreements(ContractID = UUID.fromString("7fa870d3-2119-4b41-8062-46e2d5136937"),
            CreatedDate = createdDate,
            MovedToBlockChain = true)

        val userList = ArrayList<User>()
        userList.add(userA)
        userList.add(userB)
        val judge = Judges()

        mockAgreementBC = mockAgreementBC.apply { users.add(userA) }
        mockAgreementBC.apply { users.add(userB) }

        var mockAgreementNBC = Agreements(ContractID = UUID.fromString("16e92cf0-31b1-4617-abdc-625ec01b7f1d"),
            CreatedDate = createdDate,
            MovedToBlockChain = false,
            SealedDate = sealedDate)

        mockAgreementNBC = mockAgreementNBC.apply { users.add(userA) }
        mockAgreementNBC = mockAgreementNBC.apply { users.add(userB) }

        val list = ArrayList<Agreements>()
        list.add(mockAgreementNBC)

        val judgeList = ArrayList<Judges>()
        judgeList.add(judge)

        //when
        whenever(agreementsRepository.countAgreements()).thenReturn(2)
        whenever(agreementsRepository.countAgreementsByBlockchainIDNotNull()).thenReturn(1)
        whenever(agreementsRepository.countAgreementsByBlockchainIDNull()).thenReturn(1)
        whenever(agreementsRepository.getAllBySealedDateNotNull()).thenReturn(list)
        whenever(judgesRepository.getAllByAgreement(mockAgreementNBC)).thenReturn(judgeList)
        whenever(userRepository.countAll()).thenReturn(3)
        whenever(agreementsRepository.countAgreementsByCreatedDateBefore(endDate)).thenReturn(2)
        whenever(agreementsRepository.countAgreementsBySealedDateBefore(endDate)).thenReturn(1)
        whenever(agreementsRepository.countAgreementsByCreatedDateBefore(startDate)).thenReturn(0)
        whenever(agreementsRepository.countAgreementsBySealedDateBefore(startDate)).thenReturn(0)
        whenever(agreementsRepository.countAgreementsByCreatedDateBetween(startDate, endDate)).thenReturn(2)
        whenever(agreementsRepository.countAgreementsBySealedDateBetween(startDate, endDate)).thenReturn(2)


        return statsService.detailedStats(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            , endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
    }

    @Test
    fun `Detailed stats successful`()
    {
        val startDate = Date()
        Thread.sleep(100)
        val createdDate = Date()
        Thread.sleep(100)
        val sealedDate = Date()
        Thread.sleep(100)
        val endDate = Date()

        val response = parameterizedDetailedStats(startDate, endDate, createdDate, sealedDate)

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.responseObject)
        assertFalse(response.responseObject!!.dailyStatsList.isEmpty())
    }

    @Test
    fun `Detailed stats failed`()
    {
        Thread.sleep(100)
        val endDate = Date()
        val createdDate = Date()
        Thread.sleep(100)
        val sealedDate = Date()
        val startDate = Date()
        Thread.sleep(100)

        val response = parameterizedDetailedStats(startDate, endDate, createdDate, sealedDate)
        assertEquals(response.status, ResponseStatus.FAILED)
    }
*/

}