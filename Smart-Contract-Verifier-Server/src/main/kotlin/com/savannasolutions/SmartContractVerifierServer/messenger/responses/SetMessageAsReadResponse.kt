package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus

data class SetMessageAsReadResponse(@JsonProperty("Status") val status: ResponseStatus)
