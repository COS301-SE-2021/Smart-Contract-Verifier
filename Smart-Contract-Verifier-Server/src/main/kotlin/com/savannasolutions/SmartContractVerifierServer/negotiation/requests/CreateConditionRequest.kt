package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class CreateConditionRequest(@JsonProperty("ConditionTitle") val Title : String,
                                  @JsonProperty("ConditionDescription") val ConditionDescription: String,)
