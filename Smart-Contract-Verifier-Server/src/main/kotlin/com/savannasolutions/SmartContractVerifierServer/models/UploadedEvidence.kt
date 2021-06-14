package com.savannasolutions.SmartContractVerifierServer.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class UploadedEvidence(@Id @GeneratedValue val evidenceID: Int,
                            @OneToOne val evidence: Evidence,
                            val filename: String,
                            val fileMimeType: String,)
