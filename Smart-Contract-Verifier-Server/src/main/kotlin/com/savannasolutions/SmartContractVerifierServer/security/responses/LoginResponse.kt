package com.savannasolutions.SmartContractVerifierServer.security.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginResponse(@JsonProperty("JwtToken") val jwtToken: String,)
