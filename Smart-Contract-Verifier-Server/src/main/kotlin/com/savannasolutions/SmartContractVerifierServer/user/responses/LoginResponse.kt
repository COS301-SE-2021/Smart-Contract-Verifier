package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class LoginResponse(@JsonProperty("jwtToken") val token: String,
                         @JsonProperty("Status") val status: ResponseStatus,)
