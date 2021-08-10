package com.savannasolutions.SmartContractVerifierServer.common

import com.fasterxml.jackson.annotation.JsonProperty

data class UserResponse(@JsonProperty("PublicWalletID") val PublicWalletID: String)
