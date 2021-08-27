package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.MessageResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus

data class GetAllMessagesByUserResponse(@JsonProperty("Messages") val messages: List<MessageResponse>?=null,
                                        @JsonProperty("Status") val status: ResponseStatus
)
