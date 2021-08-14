package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateAgreementRequest(@JsonProperty("PartyA") val PartyA:String,
                                  @JsonProperty("PartyB") val PartyB:String,
                                  @JsonProperty("AgreementTitle") val Title:String,
                                  @JsonProperty("AgreementDescription") val Description: String,
                                  @JsonProperty("AgreementImageURL") val ImageURL: String,)
