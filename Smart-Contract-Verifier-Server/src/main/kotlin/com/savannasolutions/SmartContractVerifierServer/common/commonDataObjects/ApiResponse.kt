package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    @JsonProperty("Status") val status : ResponseStatus,
    @JsonProperty("ResponseObject") val responseObject : T? = null,
    @JsonProperty("Message") val message : String? = null
) {
}