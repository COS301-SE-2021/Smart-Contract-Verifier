package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*

data class SetPaymentConditionRequest(@JsonProperty("Payment") val Payment: Double,
                                      @JsonProperty("PayingUser") val PayingUser: String,)
{
    companion object{
        fun request(): ArrayList<FieldDescriptor>{
            val fieldDescriptorRequest = ArrayList<FieldDescriptor>()
            fieldDescriptorRequest.add(PayloadDocumentation.fieldWithPath("Payment").description("This is the amount needed to be paid").type("Double"))
            fieldDescriptorRequest.add(PayloadDocumentation.fieldWithPath("PayingUser").description("This is the user public wallet address who will be paying").type("String"))
            return fieldDescriptorRequest
        }
    }
}
