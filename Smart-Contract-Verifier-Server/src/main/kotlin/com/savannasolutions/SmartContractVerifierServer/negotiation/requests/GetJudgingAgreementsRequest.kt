package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class GetJudgingAgreementsRequest(@JsonProperty("WalletID") val walletID: String)
