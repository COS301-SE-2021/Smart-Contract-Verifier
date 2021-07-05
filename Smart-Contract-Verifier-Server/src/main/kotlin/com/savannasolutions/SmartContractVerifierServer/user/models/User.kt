package com.savannasolutions.SmartContractVerifierServer.user.models
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import javax.persistence.*

@Entity
@Table(name = "User")
data class User(@Id val publicWalletID: String,
                val email: String,
                val alias: String,
                @OneToMany var agreements: List<Agreements> )