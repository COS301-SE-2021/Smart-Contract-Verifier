package com.savannasolutions.SmartContractVerifierServer.stats.services

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.stats.responses.DetailedStatsResponse
import com.savannasolutions.SmartContractVerifierServer.stats.responses.GeneralStatsResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class StatsService {

    fun generalStats() : ApiResponse<GeneralStatsResponse>{
        return ApiResponse(status = ResponseStatus.FAILED, message = "not implemented yet")
    }

    fun detailedStats(startDate: Date, endDate: Date) : ApiResponse<DetailedStatsResponse>{
        return ApiResponse(status = ResponseStatus.FAILED, message = "not implemented yet")
    }

}