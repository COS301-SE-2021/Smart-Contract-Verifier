package com.savannasolutions.SmartContractVerifierServer.judges.services

import com.savannasolutions.SmartContractVerifierServer.judges.configurations.ContractConfig
import com.savannasolutions.SmartContractVerifierServer.judges.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.web3j.abi.EventEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Type
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.http.HttpService
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServlet

@Service
@EnableConfigurationProperties(ContractConfig::class)
class JudgesService constructor(val judgesRepository: JudgesRepository,
                                val agreementsRepository: AgreementsRepository,
                                val userRepository: UserRepository,
                                private val contractConfig: ContractConfig,){

    private lateinit var web3j: Web3j

    @PostConstruct
    fun initEventListener(){
        web3j = Web3j.build(HttpService(contractConfig.nodeAddress))

        val createFilter = EthFilter(DefaultBlockParameterName.EARLIEST,
                               DefaultBlockParameterName.LATEST,
                               contractConfig.contractId)
        val creationEvent = Event("CreateAgreement", contractConfig.creationList)
        createFilter.addSingleTopic(EventEncoder.encode(creationEvent))
        web3j.ethLogFlowable(createFilter).subscribe { event ->
            var creationData = FunctionReturnDecoder.decode(event.data,
            contractConfig.creationList as MutableList<TypeReference<Type<Any>>>?)
            //describe how to use data to seal an agreement
        }

        val juryFilter = EthFilter(DefaultBlockParameterName.EARLIEST,
            DefaultBlockParameterName.LATEST,
            contractConfig.contractId)
        val juryAssignedEvent = Event("JuryAssigned", contractConfig.juryList)
        juryFilter.addSingleTopic(EventEncoder.encode(juryAssignedEvent))
        web3j.ethLogFlowable(juryFilter).subscribe { event ->
            var juryData = FunctionReturnDecoder.decode(event.data,
                contractConfig.juryList as MutableList<TypeReference<Type<Any>>>?)
            //use data to assign a jury
        }

    }

}