package com.savannasolutions.SmartContractVerifierServer.judges.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ContractConfig {
    @Value("com.unison.node-address")
    var nodeAddress: String = ""

    @Value("com.unison.contractId")
    var contractId: String = ""
}