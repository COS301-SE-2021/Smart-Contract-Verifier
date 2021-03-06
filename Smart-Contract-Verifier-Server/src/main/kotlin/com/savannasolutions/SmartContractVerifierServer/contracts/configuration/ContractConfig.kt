package com.savannasolutions.SmartContractVerifierServer.contracts.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String


@ConfigurationProperties("com.unison.blockchain")
@ConstructorBinding
data class ContractConfig(var nodeAddress: String, var contractId: String, var useblockchain: String){

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