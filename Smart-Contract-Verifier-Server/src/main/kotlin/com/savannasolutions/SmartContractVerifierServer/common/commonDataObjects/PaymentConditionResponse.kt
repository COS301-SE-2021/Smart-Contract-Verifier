package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import java.util.*

data class PaymentConditionResponse(@JsonProperty("ID") val conditionID: UUID,
                                    @JsonProperty("Amount") val amount: Double,
                                    @JsonProperty("Payer") val payerWalletID: String,
                                    @JsonProperty("ConditionStatus") val conditionStatus: Enum<ConditionStatus>,)
