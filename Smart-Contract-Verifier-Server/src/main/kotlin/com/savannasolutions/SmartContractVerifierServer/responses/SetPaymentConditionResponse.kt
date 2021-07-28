package com.savannasolutions.SmartContractVerifierServer.responses

import java.util.*

data class SetPaymentConditionResponse(val conditionID: UUID?,
                                       val  status: Enum<ResponseStatus>,)
