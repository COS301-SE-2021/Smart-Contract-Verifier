package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class RetrieveUserInfoRequest(@JsonProperty("UserID") val UserID:String,)
