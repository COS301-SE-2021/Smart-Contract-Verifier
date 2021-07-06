package com.savannasolutions.SmartContractVerifierServer.negotiation.models

import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.util.*
import javax.persistence.*

@Entity
data class Agreements(@Id @GeneratedValue val ContractID:UUID,
                      var blockchainID: String? = null,
                      val CreatedDate:Date,
                      var SealedDate:Date? = null,
                      var DurationConditionUUID: UUID? = null,
                      var MovedToBlockChain:Boolean,
                      var PaymentConditionUUID: UUID? = null,)
{
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agreements", orphanRemoval = true, cascade = [CascadeType.ALL])
    var conditions: List<Conditions>? = emptyList()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agreements", orphanRemoval = true, cascade = [CascadeType.ALL])
    var messages: List<Messages>? = emptyList()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PublicWalletID")
    lateinit var partyA: User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PublicWalletID")
    lateinit var partyB: User
}
