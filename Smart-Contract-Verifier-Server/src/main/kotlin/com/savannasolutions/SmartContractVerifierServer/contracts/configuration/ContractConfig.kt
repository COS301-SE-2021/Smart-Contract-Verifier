package com.savannasolutions.SmartContractVerifierServer.contracts.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*


@ConfigurationProperties("com.unison")
@ConstructorBinding
data class ContractConfig(var nodeAddress: String, var contractId: String,){

    var creationList = listOf<TypeReference<*>>(
        object : TypeReference<Address>(false) {},
        object : TypeReference<Address>(false) {},
        object : TypeReference<Uint>(false) {},
        object : TypeReference<Utf8String>(false) {},
    )

    var juryList = listOf<TypeReference<*>>(
        object : TypeReference<Uint>(false) {},
        object : TypeReference<DynamicArray<Address>>(false) {},
    )
}