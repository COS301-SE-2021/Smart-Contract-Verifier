package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.savannasolutions.SmartContractVerifierServer.common.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import java.util.*

data class GetAgreementDetailsResponse(val agreementResponse : AgreementResponse?=null,val status: Enum<ResponseStatus>,)

