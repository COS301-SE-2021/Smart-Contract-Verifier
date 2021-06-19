package com.savannasolutions.SmartContractVerifierServer.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class AcceptConditionRequest(@JsonProperty("conditionID") val conditionID: UUID,)
