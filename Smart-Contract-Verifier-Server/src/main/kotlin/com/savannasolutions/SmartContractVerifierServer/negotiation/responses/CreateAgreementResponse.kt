package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class CreateAgreementResponse(@JsonProperty("AgreementID") val agreementID: UUID? = null,
                                   @JsonProperty("Status") val  status: Enum<ResponseStatus>,)
