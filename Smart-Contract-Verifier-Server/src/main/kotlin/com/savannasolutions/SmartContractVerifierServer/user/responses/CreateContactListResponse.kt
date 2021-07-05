package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class CreateContactListResponse(val ContactListID:UUID? = null,
                                     val status:ResponseStatus)
