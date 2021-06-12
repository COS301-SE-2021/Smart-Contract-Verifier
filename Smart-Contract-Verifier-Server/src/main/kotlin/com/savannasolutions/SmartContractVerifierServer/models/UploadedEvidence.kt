package com.savannasolutions.SmartContractVerifierServer.models

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class UploadedEvidence(@Id val evidenceID: String,
                            val filename: String,
                            val fileMimeType: String,)
