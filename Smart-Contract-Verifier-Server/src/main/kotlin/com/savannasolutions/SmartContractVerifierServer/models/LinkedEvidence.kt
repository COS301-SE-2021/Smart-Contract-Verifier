package com.savannasolutions.SmartContractVerifierServer.models

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "LinkedEvidence")
data class LinkedEvidence(@OneToOne @Id val evidenceID: Evidence,
                            val evidenceUrl: String,)
