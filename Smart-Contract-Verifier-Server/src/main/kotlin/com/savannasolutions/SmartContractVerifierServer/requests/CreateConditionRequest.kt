package com.savannasolutions.SmartContractVerifierServer.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class CreateConditionRequest(@JsonProperty("PreposedUser") val PreposedUser: String,
                                  @JsonProperty("AgreementID") val AgreementID: UUID,
                                  @JsonProperty("ConditionDescription") val ConditionDescription: String,)
