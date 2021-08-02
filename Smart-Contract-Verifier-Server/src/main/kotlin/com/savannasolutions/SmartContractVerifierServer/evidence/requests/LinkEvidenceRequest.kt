package com.savannasolutions.SmartContractVerifierServer.evidence.requests

import java.util.*

data class LinkEvidenceRequest(val contractId: UUID,
                               val userID: String,
                               val url: String,)
