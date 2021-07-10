package com.savannasolutions.SmartContractVerifierServer.common

import java.util.*

data class AgreementResponse(val agreementID: UUID,
                             val agreementTitle: String,
                             val agreementDescription: String,
                             val durationID: UUID? = null,
                             val paymentID: UUID? = null,
                             val partyA: UserResponse? = null,
                             val partyB: UserResponse? = null,
                             val createdDate: Date? = null,
                             val sealedDate: Date? = null,
                             val movedToBlockchain: Boolean? = null,
                             val conditions: List<ConditionResponse>? = null,
                             val agreementImageURL: String? = null)
