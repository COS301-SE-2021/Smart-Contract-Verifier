package com.savannasolutions.SmartContractVerifierServer.common

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class AgreementResponse(@JsonProperty("AgreementID") val agreementID: UUID,
                             @JsonProperty("AgreementTitle") val agreementTitle: String,
                             @JsonProperty("AgreementDescription") val agreementDescription: String,
                             @JsonProperty("DurationID") val durationID: UUID? = null,
                             @JsonProperty("PaymentID") val paymentID: UUID? = null,
                             @JsonProperty("PartyA") val partyA: UserResponse? = null,
                             @JsonProperty("PartyB") val partyB: UserResponse? = null,
                             @JsonProperty("CreatedDate") val createdDate: Date? = null,
                             @JsonProperty("SealedDate") val sealedDate: Date? = null,
                             @JsonProperty("MovedToBlockchain") val movedToBlockchain: Boolean? = false,
                             @JsonProperty("Conditions") val conditions: List<ConditionResponse>? = null,
                             @JsonProperty("AgreementImageURL") val agreementImageURL: String? = null,
                             @JsonProperty("BlockChainID") val blockChainID: String? = null,)
