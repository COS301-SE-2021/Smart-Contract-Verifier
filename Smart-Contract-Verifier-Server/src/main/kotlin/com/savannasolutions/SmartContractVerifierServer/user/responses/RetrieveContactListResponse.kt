package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.savannasolutions.SmartContractVerifierServer.common.ContactListAliasWalletResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class RetrieveContactListResponse(val WalletAndAlias: List<ContactListAliasWalletResponse>? = null,
                                       val status: ResponseStatus,)
