package com.savannasolutions.SmartContractVerifierServer.responses

import java.util.*

data class SetDurationConditionResponse(val conditionID: UUID?,
                                        val  status: Enum<ResponseStatus>,)
