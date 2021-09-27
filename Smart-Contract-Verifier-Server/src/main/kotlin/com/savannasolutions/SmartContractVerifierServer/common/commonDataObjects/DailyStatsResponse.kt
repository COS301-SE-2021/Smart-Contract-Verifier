package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DailyStatsResponse(@JsonProperty("Date") val date : Date,
                              @JsonProperty("AgreementsCreated") val agreementsCreated : Int,
                              @JsonProperty("SuccessfulAgreements") val successfulAgreements: Int,
                              @JsonProperty("DisputedAgreements") val disputedAgreements: Int)
