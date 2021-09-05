package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ConditionResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class GetConditionDetailsResponse(@JsonProperty("ConditionResponse") val conditionResponse : ConditionResponse? = null,)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.ConditionResponse").description("This is the condition"))
            fieldDescriptorResponse.addAll(ConditionResponse.response("ResponseObject.ConditionResponse"))
            return fieldDescriptorResponse
        }
    }
}