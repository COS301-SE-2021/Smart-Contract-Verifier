package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ContactListAliasWalletResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class RetrieveContactListResponse(@JsonProperty("WalletAndAlias") val WalletAndAlias: List<ContactListAliasWalletResponse>? = null,
                                       @JsonProperty("Status") val status: ResponseStatus,)
