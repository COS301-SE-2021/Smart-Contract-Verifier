package com.savannasolutions.SmartContractVerifierServer.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class GetAllConditionsRequest(@JsonProperty("AgreementID") val AgreementID: UUID,)
