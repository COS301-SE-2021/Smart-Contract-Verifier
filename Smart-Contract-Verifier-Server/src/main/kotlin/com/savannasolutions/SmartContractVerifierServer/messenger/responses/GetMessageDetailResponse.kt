package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.savannasolutions.SmartContractVerifierServer.common.MessageResponse
import com.savannasolutions.SmartContractVerifierServer.common.MessageStatusResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.common.UserResponse
import java.util.*

data class GetMessageDetailResponse(val messageDetails: MessageResponse?=null, val status: ResponseStatus,)
