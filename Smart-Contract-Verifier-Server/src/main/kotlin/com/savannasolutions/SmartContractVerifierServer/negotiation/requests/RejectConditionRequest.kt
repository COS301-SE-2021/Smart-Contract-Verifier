package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class RejectConditionRequest(@JsonProperty("ConditionID") val conditionID:UUID,)
