package com.savannasolutions.SmartContractVerifierServer.messenger.models

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.security.configuration.SecurityConfig
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import org.hibernate.annotations.ColumnTransformer
import org.springframework.context.annotation.Import
import java.util.*
import javax.persistence.*

@Entity
data class Messages(@Id @GeneratedValue val messageID: UUID,
                    @ColumnTransformer(
                        read = "PGP_SYM_DECRYPT(cast(message as bytea), 'A VERY SECURE KEY')",
                        write = "PGP_SYM_ENCRYPT (?, 'A VERY SECURE KEY')"
                    )
                    val message: String,
                    val sendDate: Date,)
{
                    @ManyToOne(fetch = FetchType.LAZY)
                    lateinit var agreements: Agreements

                    @ManyToOne(fetch = FetchType.LAZY)
                    lateinit var sender: User

                    @OneToMany(fetch = FetchType.LAZY, mappedBy = "message", orphanRemoval = true, cascade = [CascadeType.ALL])
                    var messageStatuses: List<MessageStatus>? = emptyList()

    companion object{
        fun getSecurityKey() = System.getProperty("com.unison.SecretKey")
    }
}
