package com.savannasolutions.SmartContractVerifierServer.common

import java.util.*

data class MessageResponse(val MessageID: UUID,
                           val SendingUser: UserResponse,
                           val SendingDate: Date,
                           val AgreementID: UUID,
                           val Message: String,
                           val MessageStatuses: List<MessageStatusResponse>)
