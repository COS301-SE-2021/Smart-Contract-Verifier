package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import java.util.*
import com.fasterxml.jackson.annotation.JsonProperty

data class MessagesByAgreementResponse(@JsonProperty("AgreementID") val AgreementID: UUID,
                                        @JsonProperty("Messages") val Messages: List<MessageResponse>?= emptyList())
