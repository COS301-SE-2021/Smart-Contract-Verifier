package com.savannasolutions.SmartContractVerifierServer.security.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class LoginResponse(@JsonProperty("ResponseStatus") val responseStatus: ResponseStatus,
                         @JsonProperty("JwtToken") val jwtToken: String,)
