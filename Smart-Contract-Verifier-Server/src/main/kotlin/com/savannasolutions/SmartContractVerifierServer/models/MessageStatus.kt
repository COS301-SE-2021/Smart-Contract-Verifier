package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MessageStatus")
data class MessageStatus(@Id @GeneratedValue val MessageStatusID:UUID,
                         @OneToOne(fetch = FetchType.LAZY) val MessagesID:Messages,
                         @OneToOne(fetch = FetchType.LAZY) val RecipientID:User,
                         val ReadDate:Date,)


