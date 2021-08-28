package com.savannasolutions.SmartContractVerifierServer.evidence.interfaces

import org.springframework.web.multipart.MultipartFile
import java.io.File

interface EvidenceFileSystem {
    fun saveFile(fileToSave: MultipartFile, filename: String)

    fun retrieveFile(fileToRetrieve: String): MultipartFile

    fun deleteFile(fileToDelete: String)
}