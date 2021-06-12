package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne

enum class ConditionStatus{ACCEPTED, REJECTED, PENDING}

@Entity
data class Conditions(@Id val conditionID: Int,
                        var conditionDescription: String,
                        var conditionStatus: Enum<ConditionStatus>,
                        val proposingUser: String,
                        val proposalDate: Date,
                        @OneToOne val contract: Agreements,)
