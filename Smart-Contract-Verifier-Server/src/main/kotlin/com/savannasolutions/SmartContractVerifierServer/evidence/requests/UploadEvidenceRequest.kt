package com.savannasolutions.SmartContractVerifierServer.evidence.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.web.multipart.MultipartFile
import java.util.*
import kotlin.collections.ArrayList

data class UploadEvidenceRequest(@JsonProperty("UploadFile") val fileToUpload: MultipartFile,)
{
    companion object{
        fun request(): ArrayList<FieldDescriptor>
        {
            val fieldDescriptor = ArrayList<FieldDescriptor>()
            fieldDescriptor.add(PayloadDocumentation.fieldWithPath("UploadFile").description("This is the file to be uploaded").type("MultipartFile"))
            return fieldDescriptor
        }
    }
}
