package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class UserResponse(@JsonProperty("PublicWalletID") val PublicWalletID: String)
{
    companion object{
        fun response(Path:String): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.publicWalletID").description("This is the public wallet of the user"))
            return fieldDescriptorResponse
        }
    }
}
