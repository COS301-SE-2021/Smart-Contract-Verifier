package com.savannasolutions.SmartContractVerifierServer.user.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class RetrieveContactListRequest(@JsonProperty("ContactListID") val ContactListID : UUID,)
