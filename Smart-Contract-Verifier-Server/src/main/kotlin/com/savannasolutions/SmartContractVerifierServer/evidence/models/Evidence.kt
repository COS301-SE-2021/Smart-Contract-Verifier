package com.savannasolutions.SmartContractVerifierServer.evidence.models

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import javax.persistence.*

enum class EvidenceType{LINKED, UPLOADED}

@Entity
data class Evidence(@Id val evidenceHash: String,
                    val evidenceType: Enum<EvidenceType>,
                    @OneToOne(fetch = FetchType.LAZY) val uploadedUrl: UploadedEvidence?,){

    @ManyToOne(fetch = FetchType.LAZY,)
    lateinit var contract: Agreements

    @ManyToOne(fetch = FetchType.LAZY,)
    lateinit var user: User

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "evidence", orphanRemoval = true, cascade = [CascadeType.ALL])
    var evidenceUrl: LinkedEvidence? = null

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "evidence", orphanRemoval = true, cascade = [CascadeType.ALL])
    var uploadedEvidence: UploadedEvidence? = null
}
