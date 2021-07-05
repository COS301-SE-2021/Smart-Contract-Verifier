package com.savannasolutions.SmartContractVerifierServer.repositories

import com.savannasolutions.SmartContractVerifierServer.models.Conditions
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConditionsRepository : JpaRepository<Conditions, UUID>