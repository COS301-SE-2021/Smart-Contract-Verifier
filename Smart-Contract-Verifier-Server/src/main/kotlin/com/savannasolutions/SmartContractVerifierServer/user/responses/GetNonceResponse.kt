package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class GetNonceResponse(@JsonProperty("unsignedNonce")val nonce: Long,
                            @JsonProperty("status")val status: ResponseStatus,)
