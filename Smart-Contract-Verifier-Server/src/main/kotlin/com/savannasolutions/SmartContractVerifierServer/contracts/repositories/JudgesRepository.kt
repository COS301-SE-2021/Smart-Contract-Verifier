package com.savannasolutions.SmartContractVerifierServer.contracts.repositories

import com.savannasolutions.SmartContractVerifierServer.contracts.models.Judges
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JudgesRepository: JpaRepository<Judges, UUID> {
    fun getAllByAgreement(agreements: Agreements) : List<Judges> ?= emptyList()
    fun getAllByJudge(judge: User) : List<Judges> ?= emptyList()
}