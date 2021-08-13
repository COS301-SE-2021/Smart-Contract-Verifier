package com.savannasolutions.SmartContractVerifierServer.judges.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import java.util.*
import kotlin.collections.ArrayList


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