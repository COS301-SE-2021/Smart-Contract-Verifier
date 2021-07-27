package com.savannasolutions.SmartContractVerifierServer.user.models
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import javax.persistence.*

@Entity
@Table(schema = "public")
data class User(@Id val publicWalletID: String,
                val alias: String? = null,)
{
                @ManyToMany(cascade = [CascadeType.ALL])
                @JoinTable(
                    name = "user_agreement",
                    joinColumns = [JoinColumn(name = "public_WalletID")],
                    inverseJoinColumns = [JoinColumn(name = "ContractID")]
                )
                var agreements: MutableSet<Agreements> = mutableSetOf()

                @OneToMany(fetch = FetchType.LAZY, mappedBy = "proposingUser", orphanRemoval = true, cascade = [CascadeType.ALL])
                var conditions: List<Conditions>? = emptyList()

                @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.ALL])
                var contactListProfiles : List<ContactListProfile>? = emptyList()

                @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", orphanRemoval = true, cascade = [CascadeType.ALL])
                var contactList : List<ContactList>?= emptyList()
}