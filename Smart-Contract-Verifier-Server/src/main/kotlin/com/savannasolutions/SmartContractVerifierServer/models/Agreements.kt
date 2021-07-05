package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.*

@Entity
data class Agreements(@Id @GeneratedValue val ContractID:UUID,
                      var blockchainID: String? = null,
                      val PartyA: String,
                      val PartyB: String,
                      val CreatedDate:Date,
                      var SealedDate:Date? = null,
                      var DurationConditionUUID: UUID? = null,
                      var MovedToBlockChain:Boolean,
                      var PaymentConditionUUID: UUID? = null,) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agreements", orphanRemoval = true, cascade = [CascadeType.ALL])
    var conditions: List<Conditions>? = emptyList()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agreements", orphanRemoval = true, cascade = [CascadeType.ALL])
    var messages: List<Messages>? = emptyList()
}
