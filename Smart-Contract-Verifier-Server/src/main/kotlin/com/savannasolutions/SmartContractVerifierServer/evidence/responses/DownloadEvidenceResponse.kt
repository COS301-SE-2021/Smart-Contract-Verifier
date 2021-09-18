package com.savannasolutions.SmartContractVerifierServer.evidence.responses

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.io.File

data class DownloadEvidenceResponse(@JsonProperty("File") val file: File?,
                                    @JsonProperty("MimeType") val mimeType: String,)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>
        {
            val responseFieldDescriptor = ArrayList<FieldDescriptor>()
            responseFieldDescriptor.add(PayloadDocumentation.fieldWithPath("ResponseObject.File").description("This is the file that you will return").type("File").optional())
            responseFieldDescriptor.add(PayloadDocumentation.fieldWithPath("ResponseObject.MimeType").description("This is the mime type of the returned file").type("String"))
            return responseFieldDescriptor
        }
    }
}