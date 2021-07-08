package com.savannasolutions.SmartContractVerifierServer.negotiation.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "LinkedEvidence")
data class LinkedEvidence(@Id @GeneratedValue val evidenceID: UUID,
                          @OneToOne(fetch = FetchType.LAZY) val evidence: Evidence,
                          val evidenceUrl: String,)

