package com.savannasolutions.SmartContractVerifierServer.responses

import com.savannasolutions.SmartContractVerifierServer.models.ConditionStatus

import java.util.*

data class GetConditionDetailsResponse(val conditionID: UUID,
                                       val conditionDescription: String?,
                                       val proposingUser : String?,
                                        val proposalDate : Date?,
                                        val agreementID : UUID?,

                                       val conditionStatus: Enum<ConditionStatus>?,
                                        val status : ResponseStatus,)

