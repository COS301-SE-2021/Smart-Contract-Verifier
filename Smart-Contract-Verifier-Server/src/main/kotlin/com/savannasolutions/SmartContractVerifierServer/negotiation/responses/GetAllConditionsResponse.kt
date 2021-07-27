package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.savannasolutions.SmartContractVerifierServer.common.ConditionResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class GetAllConditionsResponse(val conditions: List<ConditionResponse>?,
                                    val status: ResponseStatus,)
