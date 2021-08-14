package com.savannasolutions.SmartContractVerifierServer.common

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import java.util.*

data class DurationConditionResponse(@JsonProperty("ID") val conditionID: UUID,
                                     @JsonProperty("Amount") val amount: Double,
                                     @JsonProperty("ConditionStatus") val conditionStatus: Enum<ConditionStatus> ,)
