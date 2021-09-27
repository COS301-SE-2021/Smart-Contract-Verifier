package com.savannasolutions.SmartContractVerifierServer.stats.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.DailyStatsResponse

data class DetailedStatsResponse(@JsonProperty("DailyStatsList") val dailyStatsList : List<DailyStatsResponse>){

}
