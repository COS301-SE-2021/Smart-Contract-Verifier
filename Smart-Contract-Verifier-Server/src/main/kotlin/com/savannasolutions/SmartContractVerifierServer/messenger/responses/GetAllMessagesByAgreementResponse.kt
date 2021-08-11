package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.MessageResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class GetAllMessagesByAgreementResponse(@JsonProperty("Messages") val messages: List<MessageResponse>? = null,
                                             @JsonProperty("Status") val status: ResponseStatus)
