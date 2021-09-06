package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class AddUserToContactListRequest(@JsonProperty("NewUserID") val UserID: String,
                                       @JsonProperty("NewUserAlias") val UserAlias: String,)
