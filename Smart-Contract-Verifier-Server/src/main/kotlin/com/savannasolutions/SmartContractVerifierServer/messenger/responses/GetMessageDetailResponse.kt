package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.MessageResponse
import com.savannasolutions.SmartContractVerifierServer.common.MessageStatusResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.common.UserResponse
import java.util.*

data class GetMessageDetailResponse(@JsonProperty("MessageDetails") val messageDetails: MessageResponse?=null,
                                    @JsonProperty("Status") val status: ResponseStatus,)
