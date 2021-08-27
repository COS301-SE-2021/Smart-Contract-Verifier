package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import java.util.*

data class SendMessageResponse(@JsonProperty("MessageID") val MessageID: UUID? = null,
)
