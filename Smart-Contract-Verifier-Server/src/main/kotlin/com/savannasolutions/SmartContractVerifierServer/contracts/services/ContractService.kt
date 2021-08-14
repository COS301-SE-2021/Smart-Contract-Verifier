package com.savannasolutions.SmartContractVerifierServer.contracts.services

import com.savannasolutions.SmartContractVerifierServer.contracts.configuration.ContractConfig
import com.savannasolutions.SmartContractVerifierServer.contracts.models.Judges
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.SealAgreementRequest
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
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
import java.lang.Exception
import java.lang.RuntimeException
import java.math.BigInteger
import java.util.*
import javax.annotation.PostConstruct
import kotlin.collections.ArrayList

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

                val juryFilter = EthFilter(
                    DefaultBlockParameterName.EARLIEST,
                    DefaultBlockParameterName.LATEST,
                    contractConfig.contractId
                )
                val juryAssignedEvent = Event("JuryAssigned", contractConfig.juryList)
                juryFilter.addSingleTopic(EventEncoder.encode(juryAssignedEvent))
                web3j.ethLogFlowable(juryFilter).subscribe { event ->
                    val juryData = FunctionReturnDecoder.decode(
                        event.data,
                        contractConfig.juryList as MutableList<TypeReference<Type<Any>>>?
                    )
                    assignJury(juryData[0].value as BigInteger, juryData[1].value as ArrayList<Address>)
                }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
    }

    fun assignJury(agreementIndex: BigInteger, jurors: ArrayList<Address>){
        val agreement = agreementsRepository.getAgreementsByBlockchainID(agreementIndex)
        if(agreement != null){
            jurors.forEach { address ->
                if(userRepository.existsByPublicWalletIDAllIgnoreCase(address.value)) {
                    val juror = Judges()
                    juror.agreement = agreement
                    juror.judge = userRepository.getUserByPublicWalletIDAllIgnoreCase(address.value)!!
                    judgesRepository.save(juror)
                }
            }
        }
    }
}