package com.savannasolutions.SmartContractVerifierServer.evidence.responses

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*
import kotlin.collections.ArrayList

data class LinkEvidenceResponse(@JsonProperty("EvidenceId") val evidenceId: UUID,) {
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("ResponseObject.EvidenceId").description("This is the unique id for the evidence").type("UUID"))
            return fieldDescriptorResponse
        }
    }
}