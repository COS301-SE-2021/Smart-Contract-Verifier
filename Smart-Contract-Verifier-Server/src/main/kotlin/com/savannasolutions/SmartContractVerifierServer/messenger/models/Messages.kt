package com.savannasolutions.SmartContractVerifierServer.messenger.models

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.util.*
import javax.persistence.*

@Entity
data class Messages(@Id @GeneratedValue val messageID: UUID,
                    val message: String,
                    val sendDate: Date,)
{
                    @ManyToOne(fetch = FetchType.LAZY)
                    lateinit var agreements: Agreements

                    @ManyToOne(fetch = FetchType.LAZY)
                    lateinit var sender: User
                    }
