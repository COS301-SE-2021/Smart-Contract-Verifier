package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class LinkedEvidenceDetailsResponse(@JsonProperty("EvidenceURL") val evidenceURL: String)
{
    companion object{
        fun response(Path: String): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.EvidenceURL").description("This is the url for the linked evidence. WARNING the safety of the url is not guaranteed").type("String"))
            return fieldDescriptorResponse
        }
    }
}
