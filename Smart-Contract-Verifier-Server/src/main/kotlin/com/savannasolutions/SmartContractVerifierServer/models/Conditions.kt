package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.*

enum class ConditionStatus{ACCEPTED, REJECTED, PENDING}

@Entity
@Table(name = "Conditions")
data class Conditions(@Id @GeneratedValue val conditionID: UUID,
                      var conditionDescription: String,
                      var conditionStatus: Enum<ConditionStatus>,
                      val proposingUser: String,
                      val proposalDate: Date,
                      @OneToOne(fetch = FetchType.LAZY) val contract: Agreements,)
