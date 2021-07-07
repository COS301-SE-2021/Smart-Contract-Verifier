package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class AddUserRequest(@JsonProperty("WalletID") val WalletID: String,)
