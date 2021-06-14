package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "LinkedEvidence")
data class LinkedEvidence(@Id @GeneratedValue val evidenceID: UUID,
                          @OneToOne val evidence: Evidence,
                          val evidenceUrl: String,)

