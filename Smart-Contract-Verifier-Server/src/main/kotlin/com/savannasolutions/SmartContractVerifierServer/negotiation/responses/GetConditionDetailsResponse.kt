package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.savannasolutions.SmartContractVerifierServer.common.ConditionResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus

import java.util.*

data class GetConditionDetailsResponse(val conditionResponse : ConditionResponse? = null,
                                       val status : ResponseStatus,)