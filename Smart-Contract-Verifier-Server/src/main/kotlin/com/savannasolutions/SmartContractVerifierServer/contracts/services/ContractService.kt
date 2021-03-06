package com.savannasolutions.SmartContractVerifierServer.contracts.services

import com.savannasolutions.SmartContractVerifierServer.contracts.configuration.ContractConfig
import com.savannasolutions.SmartContractVerifierServer.contracts.models.Judges
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.SealAgreementRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import io.reactivex.disposables.Disposable
import io.reactivex.subscribers.DisposableSubscriber
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.web3j.abi.EventEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Type
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.http.HttpService
import java.math.BigInteger
import java.util.*
import javax.annotation.PostConstruct

@Service
@EnableScheduling
@EnableConfigurationProperties(ContractConfig::class)
class ContractService constructor(val judgesRepository: JudgesRepository,
                                  val agreementsRepository: AgreementsRepository,
                                  val userRepository: UserRepository,
                                  val negotiationService: NegotiationService,
                                  private val contractConfig: ContractConfig,){

    private lateinit var web3j: Web3j
    lateinit var juryFilter: EthFilter
    lateinit var createFilter: EthFilter
    var jurySubscriber: Disposable? = null
    var createSubscriber: Disposable? = null

    @PostConstruct
    @Scheduled(fixedRate = 60000)
    fun initEventListener() {
        if(contractConfig.useblockchain == "true") {
            try {
                web3j = Web3j.build(HttpService(contractConfig.nodeAddress))

                createFilter = EthFilter(
                    DefaultBlockParameterName.EARLIEST,
                    DefaultBlockParameterName.LATEST,
                    contractConfig.contractId
                )
                val creationEvent = Event("CreateAgreement", contractConfig.creationList)
                createFilter.addSingleTopic(EventEncoder.encode(creationEvent))

                juryFilter = EthFilter(
                    DefaultBlockParameterName.EARLIEST,
                    DefaultBlockParameterName.LATEST,
                    contractConfig.contractId
                )
                val juryAssignedEvent = Event("JuryAssigned", contractConfig.juryList)
                juryFilter.addSingleTopic(EventEncoder.encode(juryAssignedEvent))

                registerSubscriber('J')
                registerSubscriber('C')

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun registerSubscriber(type: Char){
        when(type){
            'J' -> jurySubscriber?.dispose()
            'C' -> createSubscriber?.dispose()
        }

        when(type){
            'J' -> jurySubscriber = web3j.ethLogFlowable(juryFilter).subscribe(
                {event->
                    assignJuryFromEvent(event)
                },
                {err->
                    println("Blockchain unreachable: ${err.message}")
                    Thread.sleep(10000)
                    registerSubscriber('C')
                },
            )
            'C' ->
                createSubscriber = web3j.ethLogFlowable(createFilter).subscribe(
                { event ->
                    sealAgreementFromEvent(event)
                },
                { err->
                    println("Blockchain unreachable: ${err.message}")
                    Thread.sleep(10000)
                    registerSubscriber('C')
                }
            )
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
                        println("Jury Assigned: ${agreement.ContractID}, ${address.value}")
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