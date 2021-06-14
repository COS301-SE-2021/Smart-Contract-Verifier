package com.savannasolutions.SmartContractVerifierServer.models
import javax.persistence.*

@Entity
data class User(@Id val publicWalletID: String,
                val email: String,
                val alias: String,
                @OneToMany var agreements: List<Agreements> )
