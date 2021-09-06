package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

data class SetDurationConditionRequest(@JsonProperty("Duration") val Duration: Duration,)
{
    companion object{
        fun request(): ArrayList<FieldDescriptor>{
            val fieldDescriptorRequest = ArrayList<FieldDescriptor>()
            fieldDescriptorRequest.add(PayloadDocumentation.fieldWithPath("Duration").description("This is the duration of the agreement").type("Duration"))
            return fieldDescriptorRequest
        }
    }
}
