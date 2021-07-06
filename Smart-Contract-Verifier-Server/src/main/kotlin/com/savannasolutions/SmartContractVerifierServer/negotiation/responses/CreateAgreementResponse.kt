package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class CreateAgreementResponse(val agreementID: UUID? = null,
                                   val  status: Enum<ResponseStatus>,)
