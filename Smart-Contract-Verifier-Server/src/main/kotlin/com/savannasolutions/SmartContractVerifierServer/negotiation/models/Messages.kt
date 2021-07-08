package com.savannasolutions.SmartContractVerifierServer.negotiation.models

import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Messages")
data class Messages(@Id val messageID: UUID,
                    @OneToOne(fetch = FetchType.LAZY) val sender: User,
                    val message: String,
                    val sendDate: Date,
                    @OneToOne(fetch = FetchType.LAZY) val contract: Agreements,)
