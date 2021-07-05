package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.user.models.ContactList

data class RetrieveContactListResponse(val WalletIDs: List<ContactList>? = null,
                                        val status: ResponseStatus,)
