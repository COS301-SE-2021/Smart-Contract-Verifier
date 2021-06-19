package com.savannasolutions.SmartContractVerifierServer.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class GetAgreementDetailsRequest(@JsonProperty("AgreementID") val AgreementID: UUID,)
