package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class AddUserRequest(@JsonProperty("UserEmail") val UserEmail: String,
                          @JsonProperty("WalletID") val WalletID: String,)
