package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class AddUserToContactListRequest(@JsonProperty("NewUserID") val UserID: String,
                                       @JsonProperty("ContactListID") val ContactListID: UUID,
                                       @JsonProperty("NewUserAlias") val UserAlias: String,)
