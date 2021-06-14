package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "ContactList")
data class ContactList(@Id val contactListID: UUID,
                        val ownerID: String,
                        var contactListName: String)
