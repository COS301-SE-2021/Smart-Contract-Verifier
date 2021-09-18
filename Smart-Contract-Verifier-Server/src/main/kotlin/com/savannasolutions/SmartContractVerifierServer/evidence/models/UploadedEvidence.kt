package com.savannasolutions.SmartContractVerifierServer.evidence.models

import java.util.*
import javax.persistence.*

@Entity
data class UploadedEvidence(@Id @GeneratedValue val evidenceID: UUID,
                            val filename: String,
                            val fileMimeType: String,
                            val originalFilename: String)
{
    @OneToOne(fetch = FetchType.LAZY)
    lateinit var evidence: Evidence
}

