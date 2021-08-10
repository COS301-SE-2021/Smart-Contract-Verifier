package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class UserExistsRequest(@JsonProperty("WalletID") val walletID: String)
