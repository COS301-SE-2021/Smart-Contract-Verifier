package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ContactListIDContactListNameResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class RetrieveUserContactListResponse(@JsonProperty("ContactListInfo") val ContactListInfo: List<ContactListIDContactListNameResponse>?= null,
                                           @JsonProperty("Status") val status: ResponseStatus,)
