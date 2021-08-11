package com.savannasolutions.SmartContractVerifierServer.messenger.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SendMessageRequest(@JsonProperty("SendingUser") val SendingUser: String,
                              @JsonProperty("AgreementID") val AgreementID: UUID,
                              @JsonProperty("Message") val Message: String)
