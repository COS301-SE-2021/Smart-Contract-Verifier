package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.savannasolutions.SmartContractVerifierServer.common.MessageResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class GetAllMessagesByAgreementResponse(val messages: List<MessageResponse>?, val status: ResponseStatus)
