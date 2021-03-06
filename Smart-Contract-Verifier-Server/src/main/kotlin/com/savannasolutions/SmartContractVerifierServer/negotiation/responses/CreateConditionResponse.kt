package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*

data class CreateConditionResponse(@JsonProperty("ConditionID") val conditionID: UUID? = null,)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("ResponseObject.ConditionID").description("This is the unique ID of the condition").type("UUID"))
            return fieldDescriptorResponse
        }
    }
}
