package com.savannasolutions.SmartContractVerifierServer.judges.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConfigurationProperties("com.unison")
@ConstructorBinding
data class ContractConfig(var nodeAddress: String, var contractId: String,)