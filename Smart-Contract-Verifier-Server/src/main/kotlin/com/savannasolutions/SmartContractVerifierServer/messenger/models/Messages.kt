package com.savannasolutions.SmartContractVerifierServer.messenger.models

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.util.*
import javax.persistence.*

@Entity
data class Messages(@Id val messageID: UUID,
                    @OneToOne(fetch = FetchType.LAZY) val sender: User,
                    val message: String,
                    val sendDate: Date,
                    @OneToOne(fetch = FetchType.LAZY) val contract: Agreements,)
