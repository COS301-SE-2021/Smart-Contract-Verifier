package com.savannasolutions.SmartContractVerifierServer.messenger.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SendMessageRequest(@JsonProperty("Message") val Message: String)
