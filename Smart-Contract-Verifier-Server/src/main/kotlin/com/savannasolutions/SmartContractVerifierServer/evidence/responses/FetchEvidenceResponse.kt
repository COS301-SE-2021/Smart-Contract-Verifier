package com.savannasolutions.SmartContractVerifierServer.evidence.responses

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class FetchEvidenceResponse(@JsonProperty("EvidenceUrl") val evidenceUrl: String?,){
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("ResponseObject.EvidenceUrl").description("Ths is the url to the evidence").type("String").optional())
            return fieldDescriptorResponse
        }
    }
}
