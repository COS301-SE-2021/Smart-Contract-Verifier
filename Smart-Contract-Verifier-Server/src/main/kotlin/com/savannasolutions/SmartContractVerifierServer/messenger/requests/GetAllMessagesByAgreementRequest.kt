package com.savannasolutions.SmartContractVerifierServer.messenger.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class GetAllMessagesByAgreementRequest(@JsonProperty("AgreementID") val AgreementID: UUID,
                                            @JsonProperty("RequestingUser") val RequestingUser: String,)
