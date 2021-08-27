package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ContactListAliasWalletResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus

data class RetrieveContactListResponse(@JsonProperty("WalletAndAlias") val WalletAndAlias: List<ContactListAliasWalletResponse>? = null,
                                       @JsonProperty("Status") val status: ResponseStatus,)
