package com.savannasolutions.SmartContractVerifierServer.common

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class MessageStatusResponse(@JsonProperty("RecipientID") val RecipientID: String,
                                 @JsonProperty("Read") val Read: Boolean,
                                 @JsonProperty("ReadDate") val ReadDate: Date?)
