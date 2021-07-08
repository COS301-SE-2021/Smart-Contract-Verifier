package com.savannasolutions.SmartContractVerifierServer.user.models

import java.util.*
import javax.persistence.*

@Entity
data class ContactList(@Id @GeneratedValue val contactListID: UUID?=null,
                       val contactListName: String,)
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    lateinit var owner: User

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contactlist", orphanRemoval = true, cascade = [CascadeType.ALL])
    var contactListProfiles : List<ContactListProfile>?= emptyList()
}
