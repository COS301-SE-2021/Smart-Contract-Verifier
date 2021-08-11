package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import java.util.*

data class CreateContactListResponse(@JsonProperty("ContactListID") val ContactListID:UUID? = null,
                                     @JsonProperty("Status") val status:ResponseStatus)
