package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ConditionResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class GetAllConditionsResponse(@JsonProperty("Conditions") val conditions: List<ConditionResponse>? = null,)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.Conditions[]").description("This is the list of conditions"))
            fieldDescriptorResponse.addAll(ConditionResponse.response("ResponseObject.Conditions[]"))
            return fieldDescriptorResponse
        }

        fun responseEmpty(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.Conditions[]").description("This is the list of conditions"))
            return fieldDescriptorResponse
        }
    }
}
