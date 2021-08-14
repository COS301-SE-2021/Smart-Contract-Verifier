package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ConditionResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus

import java.util.*

data class GetConditionDetailsResponse(@JsonProperty("ConditionResponse") val conditionResponse : ConditionResponse? = null,
                                       @JsonProperty("Status") val status : ResponseStatus,)