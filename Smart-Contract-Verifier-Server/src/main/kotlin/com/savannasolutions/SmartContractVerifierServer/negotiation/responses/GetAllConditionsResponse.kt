package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class GetAllConditionsResponse(val conditions: List<UUID>?,
                                    val status: ResponseStatus,)
