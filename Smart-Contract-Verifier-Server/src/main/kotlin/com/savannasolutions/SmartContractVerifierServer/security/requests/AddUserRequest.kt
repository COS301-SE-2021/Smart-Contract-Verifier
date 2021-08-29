package com.savannasolutions.SmartContractVerifierServer.security.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class AddUserRequest(@JsonProperty("WalletID") val WalletID: String,
                          @JsonProperty("Alias") val Alias: String,)
