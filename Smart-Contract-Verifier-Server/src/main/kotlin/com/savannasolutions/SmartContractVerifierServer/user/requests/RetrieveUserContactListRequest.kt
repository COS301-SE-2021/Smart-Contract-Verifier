package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class RetrieveUserContactListRequest(@JsonProperty("UserID") val UserID: String,)
