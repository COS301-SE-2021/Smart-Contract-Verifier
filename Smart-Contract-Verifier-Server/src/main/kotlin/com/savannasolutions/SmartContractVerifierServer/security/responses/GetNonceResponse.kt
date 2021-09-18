package com.savannasolutions.SmartContractVerifierServer.security.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class GetNonceResponse(@JsonProperty("UnsignedNonce")val nonce: String,)
