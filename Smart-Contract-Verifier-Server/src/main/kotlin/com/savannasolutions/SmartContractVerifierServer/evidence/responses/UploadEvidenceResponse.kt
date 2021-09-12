package com.savannasolutions.SmartContractVerifierServer.evidence.responses

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*
import kotlin.collections.ArrayList

data class UploadEvidenceResponse(@JsonProperty("EvidenceID") val evidenceID: UUID)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("ResponseObject.EvidenceHash").description("This is the hash for this evidence").type("String"))
            return fieldDescriptorResponse
        }
    }
}
