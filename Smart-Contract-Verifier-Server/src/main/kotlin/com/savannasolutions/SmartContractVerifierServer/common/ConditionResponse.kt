package com.savannasolutions.SmartContractVerifierServer.common

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import java.util.*

data class ConditionResponse(val conditionID: UUID,
                             val conditionDescription: String? = null,
                             val proposingUser : UserResponse? = null,
                             val proposalDate : Date? = null,
                             val agreementID : UUID? = null,
                             val conditionStatus: Enum<ConditionStatus>? = null,
                             val conditionTitle: String? = null,)
