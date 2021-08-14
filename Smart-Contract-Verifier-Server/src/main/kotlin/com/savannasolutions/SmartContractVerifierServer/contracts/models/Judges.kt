package com.savannasolutions.SmartContractVerifierServer.contracts.models

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.util.*
import javax.persistence.*

@Entity
data class Judges(@Id @GeneratedValue val JudgeAgreementID: UUID = UUID.fromString("f69c1d5e-f3dc-4cc9-8058-f8528d5ed635"))
{
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var judge : User

    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var agreement : Agreements
}
