package com.savannasolutions.SmartContractVerifierServer.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SealAgreementRequest(@JsonProperty("AgreementID") val AgreementID: UUID,)
