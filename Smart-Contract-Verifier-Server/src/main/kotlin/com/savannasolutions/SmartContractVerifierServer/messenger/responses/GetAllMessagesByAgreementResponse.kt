package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.MessageResponse

data class GetAllMessagesByAgreementResponse(@JsonProperty("Messages") val messages: List<MessageResponse>? = null,)
