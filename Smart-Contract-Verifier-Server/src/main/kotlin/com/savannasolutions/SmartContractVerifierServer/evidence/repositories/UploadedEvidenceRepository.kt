package com.savannasolutions.SmartContractVerifierServer.evidence.repositories

import com.savannasolutions.SmartContractVerifierServer.evidence.models.UploadedEvidence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UploadedEvidenceRepository: JpaRepository<UploadedEvidence, UUID> {
}