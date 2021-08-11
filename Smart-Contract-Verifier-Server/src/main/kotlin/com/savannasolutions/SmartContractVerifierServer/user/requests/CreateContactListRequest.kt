package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateContactListRequest(@JsonProperty("UserID") val UserID:String,
                                    @JsonProperty("ContactListName") val ContactListName:String,)
