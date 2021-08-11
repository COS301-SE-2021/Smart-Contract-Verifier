package com.savannasolutions.SmartContractVerifierServer.common

import java.util.*

data class MessageStatusResponse(val RecipientID: String,
                                 val Read: Boolean, val ReadDate: Date?)
