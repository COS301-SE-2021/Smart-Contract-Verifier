package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "UploadedEvidence")
data class UploadedEvidence(@Id @GeneratedValue val evidenceID: UUID,
                            @OneToOne val evidence: Evidence,
                            val filename: String,
                            val fileMimeType: String,)

