package com.savannasolutions.SmartContractVerifierServer.security.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class AddUserRequest(@JsonProperty("WalletID") val WalletID: String,
                          @JsonProperty("Alias") val Alias: String,)
{
    companion object{
        fun request(): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorRequest = ArrayList<FieldDescriptor>()
            fieldDescriptorRequest.add(PayloadDocumentation.fieldWithPath("WalletID").description("This is the public wallet address of the new user").type("String"))
            fieldDescriptorRequest.add(PayloadDocumentation.fieldWithPath("Alias").description("This is the alias of the new user").type("String"))
            return fieldDescriptorRequest
        }
    }
}
