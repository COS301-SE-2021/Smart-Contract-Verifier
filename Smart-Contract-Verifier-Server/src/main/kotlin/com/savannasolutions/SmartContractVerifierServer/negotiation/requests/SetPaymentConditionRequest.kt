package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SetPaymentConditionRequest(@JsonProperty("ProposedUser") val PreposedUser: String,
                                      @JsonProperty("AgreementID") val AgreementID: UUID,
                                      @JsonProperty("Payment") val Payment: Double,)
