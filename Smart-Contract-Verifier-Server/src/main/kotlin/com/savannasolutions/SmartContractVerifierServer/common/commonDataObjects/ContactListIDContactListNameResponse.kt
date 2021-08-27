package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ContactListIDContactListNameResponse(@JsonProperty("ContactListName") val contactListName : String,
                                                @JsonProperty("ContactListID") val contactListID: UUID)
