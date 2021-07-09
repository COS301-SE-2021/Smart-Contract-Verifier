package com.savannasolutions.SmartContractVerifierServer.user.models
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import javax.persistence.*

@Entity
data class User(@Id val publicWalletID: String,
                val alias: String,)
{
                @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.ALL])
                var agreements: List<Agreements>? = emptyList()

                @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.ALL])
                var conditions: List<Conditions>? = emptyList()

                @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.ALL])
                var contactListProfiles : List<ContactListProfile>? = emptyList()

                @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.ALL])
                var contactList : List<ContactList>?= emptyList()
}