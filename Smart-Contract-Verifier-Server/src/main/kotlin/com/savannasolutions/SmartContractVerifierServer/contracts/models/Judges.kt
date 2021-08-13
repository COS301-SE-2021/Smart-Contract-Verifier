package com.savannasolutions.SmartContractVerifierServer.contracts.models

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.util.*
import javax.persistence.*

@Entity
data class Judges(@Id @GeneratedValue val JudgeAgreementID: UUID)
{
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var judge : User

    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var agreement : Agreements
}
