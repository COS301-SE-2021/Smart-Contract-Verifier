package com.savannasolutions.SmartContractVerifierServer.evidence.implementations

import com.savannasolutions.SmartContractVerifierServer.evidence.interfaces.EvidenceFileSystem
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.io.File

@Component
class EvidenceFileSystemS3Implementation: EvidenceFileSystem{
    override fun saveFile(fileToSave: File) {
        TODO("Not yet implemented")
    }

    override fun retrieveFile(fileToRetrieve: String) {
        TODO("Not yet implemented")
    }

    override fun deleteFile(fileToDelete: String) {
        TODO("Not yet implemented")
    }

}