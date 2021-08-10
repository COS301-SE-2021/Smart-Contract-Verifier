package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class SetPaymentConditionResponse(@JsonProperty("ConditionID") val conditionID: UUID?,
                                       @JsonProperty("Status") val  status: Enum<ResponseStatus>,)
