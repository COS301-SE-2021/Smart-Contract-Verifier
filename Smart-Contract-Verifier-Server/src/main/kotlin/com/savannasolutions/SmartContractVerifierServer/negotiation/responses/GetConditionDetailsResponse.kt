package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus

import java.util.*

data class GetConditionDetailsResponse(val conditionID: UUID,
                                       val conditionDescription: String? = null,
                                       val proposingUser : String? = null,
                                       val proposalDate : Date? = null,
                                       val agreementID : UUID? = null,
                                       val conditionStatus: Enum<ConditionStatus>? = null,
                                       val status : ResponseStatus,)