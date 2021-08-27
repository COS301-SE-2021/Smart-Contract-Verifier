package com.savannasolutions.SmartContractVerifierServer.evidence.models

import java.util.*
import javax.persistence.*

@Entity
data class LinkedEvidence(@Id @GeneratedValue val evidenceID: UUID,
                          @OneToOne(fetch = FetchType.LAZY) val evidence: Evidence,
                          val evidenceUrl: String,)

