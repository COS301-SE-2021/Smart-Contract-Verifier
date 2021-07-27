package com.savannasolutions.SmartContractVerifierServer.messenger.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class GetMessageDetailsRequest(@JsonProperty("MessageID") val MessageID: UUID)
