package com.savannasolutions.SmartContractVerifierServer.models
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(@Id val publicWalletID: String,
                val email: String,
                val alias: String,)
