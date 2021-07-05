package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class RetrieveUserInfoResponse(val UserID:String,
                                    val UserEmail:String?=null,
                                    val status: ResponseStatus)
