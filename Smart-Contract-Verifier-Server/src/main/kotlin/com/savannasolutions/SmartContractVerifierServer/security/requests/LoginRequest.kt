package com.savannasolutions.SmartContractVerifierServer.security.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequest(@JsonProperty("SignedNonce") val signedNonce: String,)
