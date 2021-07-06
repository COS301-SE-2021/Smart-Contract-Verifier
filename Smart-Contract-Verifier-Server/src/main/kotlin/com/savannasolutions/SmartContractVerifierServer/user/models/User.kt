package com.savannasolutions.SmartContractVerifierServer.user.models
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import javax.persistence.*

@Entity
data class User(@Id val publicWalletID: String,
                val email: String,
                val alias: String,)
{
                @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.ALL])
                var agreements: List<Agreements>? = emptyList()
}