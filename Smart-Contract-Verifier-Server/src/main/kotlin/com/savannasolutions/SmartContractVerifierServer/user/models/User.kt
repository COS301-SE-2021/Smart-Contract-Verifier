package com.savannasolutions.SmartContractVerifierServer.user.models
import com.savannasolutions.SmartContractVerifierServer.contracts.models.Judges
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import javax.persistence.*

@Entity
@Table(schema = "public")
data class User(@Id val publicWalletID: String,
                val alias: String? = null,
                var nonce: Long = 0,)
{
                @ManyToMany(cascade = [CascadeType.PERSIST])
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

                @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender", orphanRemoval = true, cascade = [CascadeType.ALL])
                var messageList : List<Messages>?= emptyList()

                @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipient", orphanRemoval = true, cascade = [CascadeType.ALL])
                var messageStatusList: List<MessageStatus>?= emptyList()

                @OneToMany(fetch = FetchType.LAZY, mappedBy = "judge", orphanRemoval = true, cascade = [CascadeType.ALL])
                var judgingList : List<Judges>?= emptyList()

                @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = [CascadeType.ALL])
                var evidenceList: List<Evidence>? = emptyList()
}