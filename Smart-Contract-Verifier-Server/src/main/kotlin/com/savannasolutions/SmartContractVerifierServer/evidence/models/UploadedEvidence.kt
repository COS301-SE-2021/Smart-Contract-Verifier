package com.savannasolutions.SmartContractVerifierServer.evidence.models

import java.util.*
import javax.persistence.*

@Entity
data class UploadedEvidence(@Id @GeneratedValue val evidenceID: UUID,
                            @OneToOne(fetch = FetchType.LAZY) val evidence: Evidence,
                            val filename: String,
                            val fileMimeType: String,)

