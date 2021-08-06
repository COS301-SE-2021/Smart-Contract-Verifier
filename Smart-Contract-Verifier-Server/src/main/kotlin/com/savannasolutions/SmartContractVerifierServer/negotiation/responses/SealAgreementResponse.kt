package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class SealAgreementResponse(@JsonProperty("Status") val status: Enum<ResponseStatus>,)
