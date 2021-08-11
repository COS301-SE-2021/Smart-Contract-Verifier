package com.savannasolutions.SmartContractVerifierServer.messenger.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SetMessageAsReadRequest(@JsonProperty("MessageID") val MessageID: UUID,
                                   @JsonProperty("RecipientID") val RecipientID: String,)
