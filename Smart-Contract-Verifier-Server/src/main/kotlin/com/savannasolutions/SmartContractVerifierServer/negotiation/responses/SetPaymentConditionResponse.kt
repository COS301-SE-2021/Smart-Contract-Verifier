package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*

data class SetPaymentConditionResponse(@JsonProperty("ConditionID") val conditionID: UUID?,)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("ResponseObject.ConditionID").description("This is the unique ID for the newly created condition").type("UUID"))
            return fieldDescriptorResponse
        }
    }
}
