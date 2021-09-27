package com.savannasolutions.SmartContractVerifierServer.stats.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class GeneralStatsResponse(@JsonProperty("TotalUsers") val totalUser : Int,
                                @JsonProperty("TotalAgreements") val totalAgreements : Int,
                                @JsonProperty("NumberOfJudges") val numberOfJudges: Int,
                                @JsonProperty("SealedAgreements") val sealedAgreements: Int,
                                @JsonProperty("UnSealedAgreements") val unsealedAgreements: Int,
                                @JsonProperty("AverageNegotiationPeriod") val averageNegotiationPeriod: Double,
                                @JsonProperty("ConcludedAgreements") val concludedAgreements: Int,
                                @JsonProperty("") val disputedAgreements: Int) {
}