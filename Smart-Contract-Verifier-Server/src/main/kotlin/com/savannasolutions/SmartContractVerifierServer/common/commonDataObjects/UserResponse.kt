package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty

data class UserResponse(@JsonProperty("PublicWalletID") val PublicWalletID: String)
