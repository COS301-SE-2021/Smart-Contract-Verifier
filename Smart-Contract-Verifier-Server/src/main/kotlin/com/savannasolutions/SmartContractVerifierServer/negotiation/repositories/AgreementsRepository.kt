package com.savannasolutions.SmartContractVerifierServer.negotiation.repositories

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AgreementsRepository : JpaRepository<Agreements, UUID>{
    fun getAllByPartyAOrPartyB(partyA: User, partyB: User): List<Agreements>
}