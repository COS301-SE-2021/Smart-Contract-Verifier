package com.savannasolutions.SmartContractVerifierServer.security.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus

data class AddUserResponse(@JsonProperty("Status") val status: ResponseStatus)
