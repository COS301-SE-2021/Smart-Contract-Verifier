package com.savannasolutions.SmartContractVerifierServer.evidence.responses

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

class LinkEvidenceResponse {
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("Status").description("Will return successful or failed").type("ENUM"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("Message").description("Contains any error messages").type("String"))
            return fieldDescriptorResponse
        }
    }
}