package com.savannasolutions.SmartContractVerifierServer.evidence.models

import java.util.*
import javax.persistence.*

@Entity
data class LinkedEvidence(@Id @GeneratedValue val evidenceID: UUID,
                          var evidenceUrl: String,)
{
    @OneToOne(fetch = FetchType.LAZY)
    lateinit var evidence: Evidence
}

