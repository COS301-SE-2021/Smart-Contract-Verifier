package com.savannasolutions.SmartContractVerifierServer.models

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

enum class EvidenceType{LINKED, UPLOADED}

@Entity
@Table(name = "Evidence")
data class Evidence(@Id val evidenceHash: String,
                    @OneToOne val contract: Agreements,
                    @OneToOne val user: User,
                    val evidenceType: Enum<EvidenceType>,
                    @OneToOne val evidenceUrl: LinkedEvidence?,
                    @OneToOne val uploadedUrl: UploadedEvidence?,)
