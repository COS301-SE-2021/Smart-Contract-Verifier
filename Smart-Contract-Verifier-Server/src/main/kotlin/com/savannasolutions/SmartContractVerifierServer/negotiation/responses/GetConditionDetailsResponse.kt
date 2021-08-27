package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ConditionResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus

data class GetConditionDetailsResponse(@JsonProperty("ConditionResponse") val conditionResponse : ConditionResponse? = null,
                                       @JsonProperty("Status") val status : ResponseStatus,)