package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class RetrieveUserContactListResponse(val ContactListInfo: List<Pair<UUID,String>>?= null,
                                            val status: ResponseStatus,)
