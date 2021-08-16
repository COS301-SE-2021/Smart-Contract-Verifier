package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class GetJudgingAgreementsResponse(@JsonProperty("AgreementList") val agreementList : List<AgreementResponse> ?= emptyList(),
                                        @JsonProperty("Status") val status : ResponseStatus)
