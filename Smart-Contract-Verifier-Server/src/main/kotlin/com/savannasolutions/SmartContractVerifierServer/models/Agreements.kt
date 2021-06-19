package com.savannasolutions.SmartContractVerifierServer.models

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import java.time.Duration
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Agreements")
data class Agreements(@Id @GeneratedValue val ContractID:UUID,
                      var blockchainID: String?,
                      val PartyA: String,
                      val PartyB: String,
                      val CreatedDate:Date,
                      var SealedDate:Date?,
                      var Duration:Duration?,
                      var MovedToBlockChain:Boolean,
                      @OneToMany(fetch = FetchType.EAGER, mappedBy = "contract") @Cascade(CascadeType.REMOVE) var conditions: List<Conditions>?,
                      @OneToMany(fetch = FetchType.LAZY) @Cascade(CascadeType.REMOVE) var messages: List<Messages>?,
                      var PaymentConditionUUID: UUID,)
