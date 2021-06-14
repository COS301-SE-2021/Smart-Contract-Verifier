package com.savannasolutions.SmartContractVerifierServer.repositories

import com.savannasolutions.SmartContractVerifierServer.models.Evidence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EvidenceRepository:JpaRepository<Evidence, String>