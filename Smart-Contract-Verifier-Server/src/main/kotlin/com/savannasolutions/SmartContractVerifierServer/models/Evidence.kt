package com.savannasolutions.SmartContractVerifierServer.models

import javax.persistence.*

enum class EvidenceType{LINKED, UPLOADED}

@Entity
@Table(name = "Evidence")
data class Evidence(@Id val evidenceHash: String,
                    @OneToOne(fetch = FetchType.LAZY) val contract: Agreements,
                    @OneToOne(fetch = FetchType.LAZY) val user: User,
                    val evidenceType: Enum<EvidenceType>,
                    @OneToOne(fetch = FetchType.LAZY) val evidenceUrl: LinkedEvidence?,
                    @OneToOne(fetch = FetchType.LAZY) val uploadedUrl: UploadedEvidence?,)