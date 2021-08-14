package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigInteger
import java.util.*

data class SealAgreementRequest(@JsonProperty("AgreementID") val AgreementID: UUID,
                                val index: BigInteger = BigInteger("-1"))
