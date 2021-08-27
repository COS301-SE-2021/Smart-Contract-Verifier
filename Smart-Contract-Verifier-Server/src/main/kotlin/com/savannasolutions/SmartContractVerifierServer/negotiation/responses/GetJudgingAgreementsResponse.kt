package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus

data class GetJudgingAgreementsResponse(@JsonProperty("AgreementList") val agreementList : List<AgreementResponse> ?= emptyList(),
                                        @JsonProperty("Status") val status : ResponseStatus
)
