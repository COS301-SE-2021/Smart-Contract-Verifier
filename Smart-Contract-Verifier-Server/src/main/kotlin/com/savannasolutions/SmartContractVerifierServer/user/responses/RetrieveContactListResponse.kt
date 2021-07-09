package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class RetrieveContactListResponse(val WalletAndAlias: List<Pair<UUID,String>>? = null,
                                        val status: ResponseStatus,)
