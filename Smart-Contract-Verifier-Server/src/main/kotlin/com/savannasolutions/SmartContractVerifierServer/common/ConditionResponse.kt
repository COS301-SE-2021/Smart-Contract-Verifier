package com.savannasolutions.SmartContractVerifierServer.common

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import java.util.*

data class ConditionResponse(@JsonProperty("ConditionID") val conditionID: UUID,
                             @JsonProperty("ConditionDescription") val conditionDescription: String? = null,
                             @JsonProperty("ProposingUser") val proposingUser : UserResponse? = null,
                             @JsonProperty("ProposalDate") val proposalDate : Date? = null,
                             @JsonProperty("AgreementID") val agreementID : UUID? = null,
                             @JsonProperty("ConditionStatus") val conditionStatus: Enum<ConditionStatus>? = null,
                             @JsonProperty("ConditionTitle") val conditionTitle: String? = null,)
