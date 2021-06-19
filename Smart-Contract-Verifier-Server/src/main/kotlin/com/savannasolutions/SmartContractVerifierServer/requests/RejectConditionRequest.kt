package com.savannasolutions.SmartContractVerifierServer.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class RejectConditionRequest(@JsonProperty("conditionID") val conditionID:UUID,)
