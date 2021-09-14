package com.savannasolutions.SmartContractVerifierServer.evidence.implementations

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import com.savannasolutions.SmartContractVerifierServer.evidence.interfaces.EvidenceFileSystem
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.lang.Exception
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Paths

class EvidenceFileSystemJavaFilesystemImplementation: EvidenceFileSystem {

    override fun saveFile(fileToSave: MultipartFile, filename: String) {
        val path = Paths.get(System.getProperty("user.dir")+ "\\UnisonEvidence\\$filename")
        try {
            Files.createFile(path.toAbsolutePath())
            Files.write(path, fileToSave.bytes)
        }catch (e: Exception){
            println(e.message)
        }
    }

    override fun retrieveFile(fileToRetrieve: String): File? {
        val path = Paths.get(System.getProperty("user.dir") + "\\UnisonEvidence\\$fileToRetrieve")
        try{
            val tempFile = File.createTempFile("temp", ".txt")
            tempFile.writeBytes(Files.readAllBytes(path.toAbsolutePath()))
            return tempFile
        }catch (e: Exception){
            println(e.message)
        }
        return null
    }

    override fun deleteFile(fileToDelete: String) {
        val path = Paths.get(System.getProperty("user.dir") +"\\UnisonEvidence\\$fileToDelete")
        try{
            Files.deleteIfExists(path.toAbsolutePath())
        }catch (e: Exception){
            println(e.message)
        }
    }
}