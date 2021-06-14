package com.savannasolutions.SmartContractVerifierServer.models

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UploadedEvidenceRepository: JpaRepository<UploadedEvidence, UUID> {
}