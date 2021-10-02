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
import java.math.BigInteger
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
    fun initEventListener() {
        if(contractConfig.useblockchain == "true") {
            try {
                web3j = Web3j.build(HttpService(contractConfig.nodeAddress))

                val createFilter = EthFilter(
                    DefaultBlockParameterName.EARLIEST,
                    DefaultBlockParameterName.LATEST,
                    contractConfig.contractId
                )
                val creationEvent = Event("CreateAgreement", contractConfig.creationList)
                createFilter.addSingleTopic(EventEncoder.encode(creationEvent))
                web3j.ethLogFlowable(createFilter).subscribe { event ->
                    sealAgreementFromEvent(event)
                }

                val juryFilter = EthFilter(
                    DefaultBlockParameterName.EARLIEST,
                    DefaultBlockParameterName.LATEST,
                    contractConfig.contractId
                )
                val juryAssignedEvent = Event("JuryAssigned", contractConfig.juryList)
                juryFilter.addSingleTopic(EventEncoder.encode(juryAssignedEvent))

                web3j.ethLogFlowable(juryFilter).subscribe { event ->
                    assignJuryFromEvent(event)
                }

            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    fun sealAgreementFromEvent(event: org.web3j.protocol.core.methods.response.Log){
        val creationData = FunctionReturnDecoder.decode(
            event.data,
            contractConfig.creationList as MutableList<TypeReference<Type<Any>>>?
        )
        negotiationService.sealAgreement(
            SealAgreementRequest(
                UUID.fromString(creationData[3].toString()),
                creationData[2].value as BigInteger
            )
        )
    }

    fun assignJuryFromEvent(event: org.web3j.protocol.core.methods.response.Log) {
        val juryData = FunctionReturnDecoder.decode(
            event.data,
            contractConfig.juryList as MutableList<TypeReference<Type<Any>>>?
        )
        assignJury(juryData[0].value as BigInteger, juryData[1].value as ArrayList<Address>)
    }

    fun assignJury(agreementIndex: BigInteger, jurors: ArrayList<Address>){
        val agreement = agreementsRepository.getAgreementsByBlockchainID(agreementIndex)
        if(agreement != null){
            jurors.forEach { address ->
                if(userRepository.existsByPublicWalletIDAllIgnoreCase(address.value)) {
                    val userAgreements = judgesRepository.getAllByJudge(userRepository.getUserByPublicWalletIDAllIgnoreCase(address.value)!!)
                    var found = false

                    userAgreements?.forEach { judges -> if(judges.agreement.ContractID == agreement.ContractID) found = true }

                    if(!found){
                        val juror = Judges()
                        juror.agreement = agreement
                        juror.judge = userRepository.getUserByPublicWalletIDAllIgnoreCase(address.value)!!
                        judgesRepository.save(juror)
                    }
                }
            }
        }
    }
}