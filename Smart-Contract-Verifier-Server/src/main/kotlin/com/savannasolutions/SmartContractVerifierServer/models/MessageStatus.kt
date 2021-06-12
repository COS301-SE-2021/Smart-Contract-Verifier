package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.*

@Entity
data class MessageStatus(@Id val MessageStatusID:Int, @OneToOne val MessagesID:Messages, @OneToOne val RecipientID:User, val ReadDate:Date,)
{}

