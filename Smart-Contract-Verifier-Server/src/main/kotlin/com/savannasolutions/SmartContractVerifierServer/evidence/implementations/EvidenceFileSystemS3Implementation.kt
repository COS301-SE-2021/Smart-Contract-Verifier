package com.savannasolutions.SmartContractVerifierServer.evidence.implementations

import com.savannasolutions.SmartContractVerifierServer.evidence.interfaces.EvidenceFileSystem
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Component
class EvidenceFileSystemS3Implementation: EvidenceFileSystem{
    override fun saveFile(fileToSave: MultipartFile, filename: String) {
        TODO("Not yet implemented")
    }

    override fun retrieveFile(fileToRetrieve: String): File? {
        TODO("Not yet implemented")
    }

    override fun deleteFile(fileToDelete: String) {
        TODO("Not yet implemented")
    }

}