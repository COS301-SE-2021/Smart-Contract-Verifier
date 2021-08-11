package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ConditionResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class GetAllConditionsResponse(@JsonProperty("Conditions") val conditions: List<ConditionResponse>?,
                                    @JsonProperty("Status") val status: ResponseStatus,)
