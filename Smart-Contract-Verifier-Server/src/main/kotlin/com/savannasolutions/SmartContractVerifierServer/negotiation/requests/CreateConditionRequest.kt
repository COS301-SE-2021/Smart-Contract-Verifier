package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class CreateConditionRequest(@JsonProperty("ProposedUser") val PreposedUser: String,
                                  @JsonProperty("ConditionTitle") val Title : String,
                                  @JsonProperty("AgreementID") val AgreementID: UUID,
                                  @JsonProperty("ConditionDescription") val ConditionDescription: String,)
