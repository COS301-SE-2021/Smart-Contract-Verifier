package com.savannasolutions.SmartContractVerifierServer.negotiation.models

import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.util.*
import javax.persistence.*

enum class ConditionStatus{ACCEPTED, REJECTED, PENDING}

@Entity
data class Conditions(@Id @GeneratedValue val conditionID: UUID,
                      var conditionDescription: String,
                      var conditionStatus: Enum<ConditionStatus>,
                      val proposalDate: Date,)
{
                      @ManyToOne(fetch = FetchType.LAZY)
                      @JoinColumn(name="ContractID")
                      lateinit var contract: Agreements

                      @ManyToOne(fetch = FetchType.LAZY)
                      @JoinColumn(name="PublicWalletID")
                      lateinit var proposingUser: User
}
