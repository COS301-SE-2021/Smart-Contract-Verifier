package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.Entity;
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Agreements(@Id val ContractID:Int, @OneToOne val PartyA:User, @OneToOne val PartyB:User, val CreatedDate:Date, var SealedDate:Date, var MovedToBlockChain:Boolean)
{

}
