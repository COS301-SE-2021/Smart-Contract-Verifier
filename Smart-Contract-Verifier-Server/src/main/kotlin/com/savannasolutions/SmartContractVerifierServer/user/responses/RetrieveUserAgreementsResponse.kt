package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus

data class RetrieveUserAgreementsResponse(@JsonProperty("Agreements") val Agreements: List<AgreementResponse>? = null,
                                          @JsonProperty("Status") val status: ResponseStatus,)
