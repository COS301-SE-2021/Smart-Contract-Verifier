package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class RetrieveUserAgreementsResponse(@JsonProperty("Agreements") val Agreements: List<AgreementResponse>? = null,
                                          @JsonProperty("Status") val status: ResponseStatus,)
