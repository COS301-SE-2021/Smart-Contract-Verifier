package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus

data class GetAgreementDetailsResponse(@JsonProperty("AgreementResponse") val agreementResponse : AgreementResponse?=null,
                                       @JsonProperty("Status") val status: Enum<ResponseStatus>,)

