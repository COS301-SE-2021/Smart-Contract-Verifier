package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class RetrieveUserAgreementsResponse(val AgreementIDs: List<UUID>? = null,
                                            val status: ResponseStatus,)
