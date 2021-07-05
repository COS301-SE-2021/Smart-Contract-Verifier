package com.savannasolutions.SmartContractVerifierServer.responses

import java.util.*

data class CreateConditionResponse(val conditionID: UUID?,
                                   val  status: Enum<ResponseStatus>,)
