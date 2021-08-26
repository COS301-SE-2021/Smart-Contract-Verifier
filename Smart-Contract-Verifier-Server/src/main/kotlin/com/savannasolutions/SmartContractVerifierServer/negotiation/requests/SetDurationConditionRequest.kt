package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Duration
import java.util.*

data class SetDurationConditionRequest(@JsonProperty("Duration") val Duration: Duration,)
