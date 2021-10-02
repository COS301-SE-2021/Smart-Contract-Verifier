package com.savannasolutions.SmartContractVerifierServer.stats.services

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.stats.responses.DetailedStatsResponse
import com.savannasolutions.SmartContractVerifierServer.stats.responses.GeneralStatsResponse
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.stereotype.Service
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
                    numberOfJudges = judgesRepository.countAll(),
                    sealedAgreements = agreementsRepository.countAgreementsByBlockchainIDNotNull(),
                    unsealedAgreements = agreementsRepository.countAgreementsByBlockchainIDNull(),
                    averageNegotiationPeriod = avg,
                    concludedAgreements = concluded,
                    disputedAgreements = disputed
                )
            )
    }

    fun detailedStats(startDate: Date, endDate: Date) : ApiResponse<DetailedStatsResponse>{
        return ApiResponse(status = ResponseStatus.FAILED, message = "not implemented yet")
    }

}