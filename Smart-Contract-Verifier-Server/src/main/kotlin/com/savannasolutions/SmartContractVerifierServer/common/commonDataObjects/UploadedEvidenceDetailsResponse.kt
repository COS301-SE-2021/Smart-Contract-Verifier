package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class UploadedEvidenceDetailsResponse(@JsonProperty("OriginalFileName") val originalFileName: String,
                                            @JsonProperty("FileMimeType") val fileMimeType: String)
{
    companion object{
        fun response(Path: String): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.OriginalFileName").description("This is the original name of the file").type("String"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.FileMimeType").description("Thus is the file's mime type").type("String"))
            return fieldDescriptorResponse
        }
    }
}
