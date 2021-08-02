package com.savannasolutions.SmartContractVerifierServer.evidence.interfaces

import org.springframework.web.multipart.MultipartFile
import java.io.File

interface EvidenceFileSystem {
    fun saveFile(fileToSave: MultipartFile)

    fun retrieveFile(fileToRetrieve: String)

    fun deleteFile(fileToDelete: String)
}