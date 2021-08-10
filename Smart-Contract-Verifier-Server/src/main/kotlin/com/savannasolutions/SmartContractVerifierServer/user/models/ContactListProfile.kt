package com.savannasolutions.SmartContractVerifierServer.user.models

import java.util.*
import javax.persistence.*

@Entity
data class ContactListProfile(@GeneratedValue @Id val ProfileID: UUID ?=null,
                              val contactAlias:String, )
{
    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "ContactListID")
    lateinit var contactList: ContactList

    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "UserID")
    lateinit var user: User
}
