package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Agreements")
data class Agreements(@Id @GeneratedValue val ContractID:Int,
                      var blockchainID: String,
                      @OneToOne val PartyA:User,
                      @OneToOne val PartyB:User,
                      val CreatedDate:Date,
                      var SealedDate:Date,
                      var MovedToBlockChain:Boolean,
                      @OneToMany var conditions: List<Conditions>,
                      @OneToMany var messages: List<Messages>,)
