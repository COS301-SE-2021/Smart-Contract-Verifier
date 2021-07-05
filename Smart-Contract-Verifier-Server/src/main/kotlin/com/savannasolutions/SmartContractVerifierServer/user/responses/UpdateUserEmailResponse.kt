package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class UpdateUserEmailResponse(val PreviousEmail:String?=null,
                                    val status: ResponseStatus,)
