package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateUserEmailRequest(@JsonProperty("UserID") val UserID: String,
                                  @JsonProperty("ReplacementEmail") val ReplacementEmail: String,)
