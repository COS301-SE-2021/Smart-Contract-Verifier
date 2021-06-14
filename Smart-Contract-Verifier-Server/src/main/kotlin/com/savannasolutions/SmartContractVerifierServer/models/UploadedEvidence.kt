package com.savannasolutions.SmartContractVerifierServer.models

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "UploadedEvidence")
data class UploadedEvidence(@OneToOne @Id val evidenceID: Evidence,
                            val filename: String,
                            val fileMimeType: String,)
