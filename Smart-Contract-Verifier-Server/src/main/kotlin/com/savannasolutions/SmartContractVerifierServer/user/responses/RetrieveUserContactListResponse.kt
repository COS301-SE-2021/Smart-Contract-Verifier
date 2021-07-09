package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.savannasolutions.SmartContractVerifierServer.common.ContactListIDContactListNameResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class RetrieveUserContactListResponse(val ContactListInfo: List<ContactListIDContactListNameResponse>?= null,
                                            val status: ResponseStatus,)
