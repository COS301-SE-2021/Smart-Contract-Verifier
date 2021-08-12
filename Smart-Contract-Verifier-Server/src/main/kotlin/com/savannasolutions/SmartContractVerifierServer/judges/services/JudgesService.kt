package com.savannasolutions.SmartContractVerifierServer.judges.services

import com.savannasolutions.SmartContractVerifierServer.judges.configurations.ContractConfig
import com.savannasolutions.SmartContractVerifierServer.judges.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.stereotype.Service
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.http.HttpService
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServlet

@Service
class JudgesService constructor(val judgesRepository: JudgesRepository,
                                val agreementsRepository: AgreementsRepository,
                                val userRepository: UserRepository,
                                private val contractConfig: ContractConfig,){

    private lateinit var web3j: Web3j

    @PostConstruct
    fun initEventListener(){
        web3j = Web3j.build(HttpService(contractConfig.nodeAddress))
        var filter = EthFilter(DefaultBlockParameterName.EARLIEST,
                               DefaultBlockParameterName.LATEST,
                               contractConfig.contractId)

    }

}