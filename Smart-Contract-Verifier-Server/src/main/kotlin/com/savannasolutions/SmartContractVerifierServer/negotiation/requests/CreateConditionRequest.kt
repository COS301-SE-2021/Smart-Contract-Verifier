package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class CreateConditionRequest(@JsonProperty("ProposedUser") val PreposedUser: String,
                                  @JsonProperty("AgreementID") val AgreementID: UUID,
                                  @JsonProperty("ConditionDescription") val ConditionDescription: String,)
