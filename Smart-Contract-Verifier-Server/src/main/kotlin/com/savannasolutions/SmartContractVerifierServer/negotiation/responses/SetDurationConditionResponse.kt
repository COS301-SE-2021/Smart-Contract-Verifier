package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*

data class SetDurationConditionResponse(@JsonProperty("ConditionID") val conditionID: UUID? = null,)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("ResponseObject.ConditionID").description("This is the unique id of the newly created duration condition").type("UUID"))
            return fieldDescriptorResponse
        }
    }
}
