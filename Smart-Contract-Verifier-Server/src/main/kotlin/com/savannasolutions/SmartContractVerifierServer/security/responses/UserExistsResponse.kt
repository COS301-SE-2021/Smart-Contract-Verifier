package com.savannasolutions.SmartContractVerifierServer.security.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class UserExistsResponse(@JsonProperty("Exists") val Exists : Boolean = false,
                                @JsonProperty("Status") val status : ResponseStatus)
