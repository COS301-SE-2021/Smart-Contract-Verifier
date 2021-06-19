package com.savannasolutions.SmartContractVerifierServer.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Duration
import java.util.*

data class SetDurationConditionRequest(@JsonProperty("ProposedUser") val PreposedUser: String,
                                       @JsonProperty("AgreementID") val AgreementID: UUID,
                                       @JsonProperty("Duration") val Duration: Duration,)
