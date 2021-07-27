package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class SendMessageResponse(val MessageID: UUID?,
                               val status: ResponseStatus)
