package com.savannasolutions.SmartContractVerifierServer.common

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ContactListAliasWalletResponse(@JsonProperty("Alias") val Alias : String,
                                          @JsonProperty("WalletID") val WalletID:String)
