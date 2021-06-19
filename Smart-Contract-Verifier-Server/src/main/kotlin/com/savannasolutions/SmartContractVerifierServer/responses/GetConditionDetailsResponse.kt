package com.savannasolutions.SmartContractVerifierServer.responses

import java.util.*

data class GetConditionDetailsResponse(val conditionID: UUID,
                                       val conditionDescription: String?,
                                       val proposingUser : String?,
                                        val proposalDate : Date?,
                                        val agreementID : UUID?,)
