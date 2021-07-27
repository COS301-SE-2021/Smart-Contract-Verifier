package com.savannasolutions.SmartContractVerifierServer.messenger.models

import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.util.*
import javax.persistence.*

@Entity
data class MessageStatus(@Id @GeneratedValue val MessageStatusID:UUID,
                         @OneToOne(fetch = FetchType.LAZY) val MessagesID: Messages,
                         @OneToOne(fetch = FetchType.LAZY) val RecipientID: User,
                         val ReadDate:Date,)


