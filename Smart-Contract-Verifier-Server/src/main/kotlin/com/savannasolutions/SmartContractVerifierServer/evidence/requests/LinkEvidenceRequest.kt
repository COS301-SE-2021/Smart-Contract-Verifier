package com.savannasolutions.SmartContractVerifierServer.evidence.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class LinkEvidenceRequest(@JsonProperty("evidenceUrl") val url: String,)
