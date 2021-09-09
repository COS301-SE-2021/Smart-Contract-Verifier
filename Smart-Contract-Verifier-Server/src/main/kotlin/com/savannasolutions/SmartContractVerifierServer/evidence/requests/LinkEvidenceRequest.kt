package com.savannasolutions.SmartContractVerifierServer.evidence.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*
import kotlin.collections.ArrayList

data class LinkEvidenceRequest(@JsonProperty("EvidenceUrl") val url: String,)
{
    companion object{
        fun request(): ArrayList<FieldDescriptor>{
            val fieldDescriptor = ArrayList<FieldDescriptor>()
            fieldDescriptor.add(PayloadDocumentation.fieldWithPath("EvidenceUrl").description("This is the url to the evidence that will be submitted").type("String"))
            return fieldDescriptor
        }
    }
}
