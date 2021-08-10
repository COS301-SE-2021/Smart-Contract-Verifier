package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class RemoveUserFromContactListRequest(@JsonProperty("RemoveUserID") val RemoveUserID: String,
                                            @JsonProperty("ContactListID") val ContactListID: UUID,)
