package com.savannasolutions.SmartContractVerifierServer.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class GetConditionDetailsRequest(@JsonProperty("conditionID") val conditionID: UUID)
