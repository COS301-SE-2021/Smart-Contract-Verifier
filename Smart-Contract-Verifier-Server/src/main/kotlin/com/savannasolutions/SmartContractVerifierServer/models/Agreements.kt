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
                      var SealedDate:Date,
                      var MovedToBlockChain:Boolean,
                      @OneToMany var conditions: List<Conditions>,
                      @OneToMany var messages: List<Messages>,)
