package com.savannasolutions.SmartContractVerifierServer.common

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class MessageResponse(@JsonProperty("MessageID") val MessageID: UUID,
                           @JsonProperty("SendingUser") val SendingUser: UserResponse,
                           @JsonProperty("SendingDate") val SendingDate: Date,
                           @JsonProperty("AgreementID") val AgreementID: UUID,
                           @JsonProperty("Message") val Message: String,
                           @JsonProperty("MessageStatuses") val MessageStatuses: List<MessageStatusResponse>)
