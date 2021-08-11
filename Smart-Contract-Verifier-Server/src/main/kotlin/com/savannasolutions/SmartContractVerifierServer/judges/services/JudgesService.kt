package com.savannasolutions.SmartContractVerifierServer.judges.services

import com.savannasolutions.SmartContractVerifierServer.judges.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class JudgesService constructor(val judgesRepository: JudgesRepository,
                                val agreementsRepository: AgreementsRepository,
                                val userRepository: UserRepository,){
}