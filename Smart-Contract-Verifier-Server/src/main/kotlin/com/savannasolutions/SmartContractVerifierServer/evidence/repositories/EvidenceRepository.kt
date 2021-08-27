package com.savannasolutions.SmartContractVerifierServer.evidence.repositories

import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EvidenceRepository:JpaRepository<Evidence, String>{
    fun getAllByContract(agreements: Agreements) : List<Evidence>
}
