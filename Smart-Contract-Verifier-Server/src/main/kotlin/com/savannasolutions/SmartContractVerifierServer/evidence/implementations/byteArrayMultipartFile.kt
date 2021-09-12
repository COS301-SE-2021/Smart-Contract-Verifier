package com.savannasolutions.SmartContractVerifierServer.evidence.implementations

import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ByteArrayMultipartFile(bytes: ByteArray) : MultipartFile {
    var fileBytes: ByteArray = bytes

    override fun getInputStream(): InputStream {
        return ByteArrayInputStream(fileBytes)
    }

    override fun getName(): String {
        //TODO("Not yet implemented")
        return "file"
    }

    override fun getOriginalFilename(): String? {
        //TODO("Not yet implemented")
        return null
    }

    override fun getContentType(): String? {
        // TODO("Not yet implemented")
        return null
    }

    override fun isEmpty(): Boolean {
        return fileBytes.isNotEmpty()
    }

    override fun getSize(): Long {
        return fileBytes.size.toLong()
    }

    override fun getBytes(): ByteArray {
        return fileBytes
    }

    override fun transferTo(p0: File) {
        FileOutputStream(p0).write(fileBytes)
    }
}