package com.savannasolutions.SmartContractVerifierServer.messenger.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class GetAllMessagesByUserRequest(@JsonProperty("RequestingUser") val RequestingUser: String)
