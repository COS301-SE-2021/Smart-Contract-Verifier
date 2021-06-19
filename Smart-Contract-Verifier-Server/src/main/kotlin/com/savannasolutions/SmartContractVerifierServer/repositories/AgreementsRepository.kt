package com.savannasolutions.SmartContractVerifierServer.repositories

import com.savannasolutions.SmartContractVerifierServer.models.Agreements
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AgreementsRepository : JpaRepository<Agreements, UUID>