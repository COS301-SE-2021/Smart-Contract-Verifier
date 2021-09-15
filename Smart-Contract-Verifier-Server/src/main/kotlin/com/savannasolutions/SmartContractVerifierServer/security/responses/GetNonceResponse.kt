package com.savannasolutions.SmartContractVerifierServer.security.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus

data class GetNonceResponse(@JsonProperty("UnsignedNonce")val nonce: String,)
