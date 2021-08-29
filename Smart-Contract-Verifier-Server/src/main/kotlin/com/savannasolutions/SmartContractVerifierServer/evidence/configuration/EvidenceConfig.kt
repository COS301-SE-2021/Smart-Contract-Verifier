package com.savannasolutions.SmartContractVerifierServer.evidence.configuration

import com.savannasolutions.SmartContractVerifierServer.evidence.implementations.EvidenceFileSystemJIMFSImplementation
import com.savannasolutions.SmartContractVerifierServer.evidence.implementations.EvidenceFileSystemJavaFilesystemImplementation
import com.savannasolutions.SmartContractVerifierServer.evidence.implementations.EvidenceFileSystemS3Implementation
import com.savannasolutions.SmartContractVerifierServer.evidence.interfaces.EvidenceFileSystem
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.annotation.PostConstruct


@ConfigurationProperties("com.unison.evidence")
@ConstructorBinding
data class EvidenceConfig(val enabledFilesystem: String,){

    lateinit var filesystem: EvidenceFileSystem

    @PostConstruct
    fun initialise(){
        filesystem = when(enabledFilesystem){
            "AWSS3" -> EvidenceFileSystemS3Implementation()
            "LOCAL" -> EvidenceFileSystemJavaFilesystemImplementation()
            else -> EvidenceFileSystemJIMFSImplementation()
        }
    }
}
