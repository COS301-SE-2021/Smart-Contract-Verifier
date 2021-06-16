package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "UploadedEvidence")
data class UploadedEvidence(@Id @GeneratedValue val evidenceID: UUID,
                            @OneToOne(fetch = FetchType.LAZY) val evidence: Evidence,
                            val filename: String,
                            val fileMimeType: String,)

