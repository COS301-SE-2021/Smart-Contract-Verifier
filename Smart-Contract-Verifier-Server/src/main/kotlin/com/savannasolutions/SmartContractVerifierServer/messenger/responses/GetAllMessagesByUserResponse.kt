package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.savannasolutions.SmartContractVerifierServer.common.MessageResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class GetAllMessagesByUserResponse(val messages: List<MessageResponse>?, val status: ResponseStatus)
