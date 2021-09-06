package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateAgreementRequest(@JsonProperty("PartyB") val PartyB:String,
                                  @JsonProperty("AgreementTitle") val Title:String,
                                  @JsonProperty("AgreementDescription") val Description: String,
                                  @JsonProperty("AgreementImageURL") val ImageURL: String,)
