package com.savannasolutions.SmartContractVerifierServer.requests

import java.util.*

data class CreateConditionRequest(val PreposedUser: String, val AgreementID: UUID, val ConditionDescription: String)
