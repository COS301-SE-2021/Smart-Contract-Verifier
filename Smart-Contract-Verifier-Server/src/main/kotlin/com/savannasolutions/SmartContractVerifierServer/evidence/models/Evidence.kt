package com.savannasolutions.SmartContractVerifierServer.evidence.models

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import javax.persistence.*

enum class EvidenceType{LINKED, UPLOADED}

@Entity
data class Evidence(@Id val evidenceHash: String,
                    @OneToOne(fetch = FetchType.LAZY) val contract: Agreements,
                    @OneToOne(fetch = FetchType.LAZY) val user: User,
                    val evidenceType: Enum<EvidenceType>,
                    @OneToOne(fetch = FetchType.LAZY) val evidenceUrl: LinkedEvidence?,
                    @OneToOne(fetch = FetchType.LAZY) val uploadedUrl: UploadedEvidence?,)
