package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import java.util.*

data class GetAgreementDetailsResponse(@JsonProperty("AgreementResponse") val agreementResponse : AgreementResponse?=null,
                                       @JsonProperty("Status") val status: Enum<ResponseStatus>,)

