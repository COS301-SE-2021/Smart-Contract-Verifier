package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Agreements")
data class Agreements(@Id @GeneratedValue val ContractID:UUID,
                      var blockchainID: String,
                      val PartyA: String,
                      val PartyB: String,
                      val CreatedDate:Date,
                      var SealedDate:Date?,
                      var Duration:Date,
                      var MovedToBlockChain:Boolean,
                      @OneToMany(fetch = FetchType.LAZY) var conditions: List<Conditions>?,
                      @OneToMany(fetch = FetchType.LAZY) var messages: List<Messages>?,)
