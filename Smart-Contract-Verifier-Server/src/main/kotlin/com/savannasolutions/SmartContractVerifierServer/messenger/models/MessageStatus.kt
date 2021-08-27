package com.savannasolutions.SmartContractVerifierServer.messenger.models

import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.util.*
import javax.persistence.*

@Entity
data class MessageStatus(@Id @GeneratedValue val MessageStatusID:UUID,
                         var ReadDate:Date?=null,)
{
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var message: Messages

    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var recipient: User
}


