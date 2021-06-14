package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "Messages")
data class Messages(@Id val messageID: String,
                    @OneToOne val sender: User,
                    val message: String,
                    val sendDate: Date,
                    @OneToOne val contract: Agreements,)
