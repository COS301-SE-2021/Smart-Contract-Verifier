package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*
import kotlin.collections.ArrayList

data class CreateConditionRequest(@JsonProperty("ConditionTitle") val Title : String,
                                  @JsonProperty("ConditionDescription") val ConditionDescription: String,)
{
    companion object{
        fun request(): ArrayList<FieldDescriptor>{
            val fieldDescriptorRequests = ArrayList<FieldDescriptor>()
            fieldDescriptorRequests.add(PayloadDocumentation.fieldWithPath("ConditionTitle").description("This is the title of the condition").type("String"))
            fieldDescriptorRequests.add(PayloadDocumentation.fieldWithPath("ConditionDescription").description("This is the description of the condition").type("String"))
            return fieldDescriptorRequests
        }
    }
}
