package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SetPaymentConditionRequest(@JsonProperty("Payment") val Payment: Double,
                                      @JsonProperty("PayingUser") val PayingUser: String,)
