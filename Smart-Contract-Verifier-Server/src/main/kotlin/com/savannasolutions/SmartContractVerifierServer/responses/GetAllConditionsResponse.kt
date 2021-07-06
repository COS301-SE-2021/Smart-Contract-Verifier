package com.savannasolutions.SmartContractVerifierServer.responses

import java.util.*

data class GetAllConditionsResponse(val conditions: List<UUID>?,
                                    val status: ResponseStatus,)
