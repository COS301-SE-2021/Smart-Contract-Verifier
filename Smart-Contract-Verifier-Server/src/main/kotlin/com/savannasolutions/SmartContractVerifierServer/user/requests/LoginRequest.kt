package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequest(@JsonProperty("UserID")val UserID: String,
                        @JsonProperty("signedNonce")val nonce: Int,)
