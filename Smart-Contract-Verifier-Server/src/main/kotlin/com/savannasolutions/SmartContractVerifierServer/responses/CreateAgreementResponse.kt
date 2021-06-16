package com.savannasolutions.SmartContractVerifierServer.responses

import java.util.*

data class CreateAgreementResponse(val agreementID: UUID?,
                                   val  status: Enum<ResponseStatus>,)
