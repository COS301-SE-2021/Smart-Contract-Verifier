package com.savannasolutions.SmartContractVerifierServer.models

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

enum class ConditionStatus{ACCEPTED, REJECTED, PENDING}

@Entity
@Table(name = "Conditions")
data class Conditions(@Id val conditionID: UUID,
                        var conditionDescription: String,
                        var conditionStatus: Enum<ConditionStatus>,
                        val proposingUser: String,
                        val proposalDate: Date,
                        @OneToOne val contract: Agreements,)
