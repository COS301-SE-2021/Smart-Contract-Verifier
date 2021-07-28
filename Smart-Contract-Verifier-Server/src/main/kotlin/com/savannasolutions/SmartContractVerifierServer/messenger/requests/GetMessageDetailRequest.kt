package com.savannasolutions.SmartContractVerifierServer.messenger.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class GetMessageDetailRequest(@JsonProperty("MessageID") val MessageID: UUID)
