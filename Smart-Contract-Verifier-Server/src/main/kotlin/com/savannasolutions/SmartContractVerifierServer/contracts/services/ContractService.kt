package com.savannasolutions.SmartContractVerifierServer.contracts.services

import com.savannasolutions.SmartContractVerifierServer.contracts.configuration.ContractConfig
import com.savannasolutions.SmartContractVerifierServer.contracts.models.Judges
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.SealAgreementRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.web3j.abi.EventEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Type
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.http.HttpService
import java.util.*
import javax.annotation.PostConstruct

@Service
@EnableConfigurationProperties(ContractConfig::class)
class ContractService constructor(val judgesRepository: JudgesRepository,
                                  val agreementsRepository: AgreementsRepository,
                                  val userRepository: UserRepository,
                                  val negotiationService: NegotiationService,
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
            val creationData = FunctionReturnDecoder.decode(event.data,
            contractConfig.creationList as MutableList<TypeReference<Type<Any>>>?)
            //describe how to use data to seal an agreement
            negotiationService.sealAgreement(SealAgreementRequest(UUID.fromString(creationData[3].toString()), creationData[3].value as Int))
        }

        val juryFilter = EthFilter(DefaultBlockParameterName.EARLIEST,
            DefaultBlockParameterName.LATEST,
            contractConfig.contractId)
        val juryAssignedEvent = Event("JuryAssigned", contractConfig.juryList)
        juryFilter.addSingleTopic(EventEncoder.encode(juryAssignedEvent))
        web3j.ethLogFlowable(juryFilter).subscribe { event ->
            val juryData = FunctionReturnDecoder.decode(event.data,
                contractConfig.juryList as MutableList<TypeReference<Type<Any>>>?)
            //use data to assign a jury
            assignJury(juryData[0].value as Int, juryData[1] as List<TypeReference<Address>>)
        }

    }
    fun assignJury(agreementIndex: Int, jurors: List<TypeReference<Address>>){
        val agreement = agreementsRepository.getAgreementsByBlockchainID(agreementIndex)
        if(agreement != null){
            jurors.forEach { address ->
                if(userRepository.existsById(address.toString())) {
                    val juror = Judges()
                    juror.agreement = agreement
                    juror.judge = userRepository.getById(address.toString())
                    judgesRepository.save(juror)
                }
            }
        }
    }
}