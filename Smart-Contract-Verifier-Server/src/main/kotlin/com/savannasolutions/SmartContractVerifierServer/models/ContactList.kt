package com.savannasolutions.SmartContractVerifierServer.models

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class ContactList(@Id val contactListID: String,
                        val ownerID: String,
                        var contactListName: String)
