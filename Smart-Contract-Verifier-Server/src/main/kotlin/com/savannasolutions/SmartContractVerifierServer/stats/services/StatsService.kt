package com.savannasolutions.SmartContractVerifierServer.stats.services

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.DailyStatsResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.stats.responses.DetailedStatsResponse
import com.savannasolutions.SmartContractVerifierServer.stats.responses.GeneralStatsResponse
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Service
class StatsService constructor(val agreementsRepository: AgreementsRepository,
                               val userRepository: UserRepository,
                                val judgesRepository: JudgesRepository) {

    fun generalStats() : ApiResponse<GeneralStatsResponse>{
        val agreementList = agreementsRepository.getAllBySealedDateNotNull()
        var avg = 0.0
        var concluded = 0
        var disputed = 0
        if(agreementList != null && agreementList.isNotEmpty())
        {
            var totalDiv : Long = 0
            for (agreement in agreementList)
            {
                totalDiv += agreement.SealedDate!!.time - agreement.CreatedDate.time
                if(judgesRepository.getAllByAgreement(agreement) == null)
                    concluded++
                else
                    disputed++
            }
            avg = totalDiv.toDouble() / agreementList.size.toDouble()
        }

        return ApiResponse(status = ResponseStatus.SUCCESSFUL,
            responseObject = GeneralStatsResponse(
                    totalAgreements = agreementsRepository.countAgreements(),
                    totalUser = userRepository.countAll(),
                    sealedAgreements = agreementsRepository.countAgreementsByBlockchainIDNotNull(),
                    unsealedAgreements = agreementsRepository.countAgreementsByBlockchainIDNull(),
                    averageNegotiationPeriod = avg,
                    concludedAgreements = concluded,
                    disputedAgreements = disputed
                )
            )
    }

    fun detailedStats(startDate: LocalDate, endDate: LocalDate) : ApiResponse<DetailedStatsResponse>{
        if(startDate > endDate)
            return ApiResponse(status = ResponseStatus.FAILED, message = "Start date is greater then end date")

        val dailyStats = ArrayList<DailyStatsResponse>()
        var ld = startDate
        var d : Date

        while(ld <= endDate)
        {
            d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val eD = Date.from(ld.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
            dailyStats.add(
                DailyStatsResponse(
                date = d,
                agreementsCreatedOnDay = agreementsRepository.countAgreementsByCreatedDateBetween(d, eD),
                agreementsCreatedUpTillDay = agreementsRepository.countAgreementsByCreatedDateBefore(d) + agreementsRepository.countAgreementsByCreatedDateBetween(d, eD),
                successfulAgreementsOnDay = agreementsRepository.countAgreementsBySealedDateBetween(d, eD),
                successfulAgreementsUpTillDay = agreementsRepository.countAgreementsBySealedDateBefore(d) + agreementsRepository.countAgreementsBySealedDateBetween(d, eD)
                )
            )
            ld = ld.plusDays(1)
        }

        val detailedStats = DetailedStatsResponse(dailyStats,
                                                    agreementsRepository.countAgreementsByCreatedDateBetween(
                                                        Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                                        Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())),
                                                    agreementsRepository.countAgreementsBySealedDateBetween(
                                                        Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                                        Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))

        return ApiResponse(status = ResponseStatus.SUCCESSFUL,
                            responseObject = detailedStats)
    }

}