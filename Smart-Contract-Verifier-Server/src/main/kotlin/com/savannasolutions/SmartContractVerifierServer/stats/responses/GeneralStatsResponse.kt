package com.savannasolutions.SmartContractVerifierServer.stats.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class GeneralStatsResponse(@JsonProperty("TotalUsers") val totalUser : Int,
                                @JsonProperty("TotalAgreements") val totalAgreements : Int,
                                @JsonProperty("NumberOfJudges") val numberOfJudges: Int,
                                @JsonProperty("SuccessfulAgreements") val successfulAgreements: Int,
                                @JsonProperty("DisputedAgreements") val disputedAgreements: Int,
                                @JsonProperty("AverageNegotiationPeriod") val averageNegotiationPeriod: Double) {
}