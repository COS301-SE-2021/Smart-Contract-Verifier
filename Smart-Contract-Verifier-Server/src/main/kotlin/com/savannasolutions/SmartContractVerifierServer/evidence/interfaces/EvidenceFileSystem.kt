package com.savannasolutions.SmartContractVerifierServer.evidence.interfaces

import java.io.File

interface EvidenceFileSystem {
    fun saveFile(fileToSave: File)

    fun retrieveFile(fileToRetrieve: String)

    fun deleteFile(fileToDelete: String)
}