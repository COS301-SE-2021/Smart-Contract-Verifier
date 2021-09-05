package com.savannasolutions.SmartContractVerifierServer.messenger.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*
import kotlin.collections.ArrayList

data class SendMessageRequest(@JsonProperty("Message") val Message: String)
{
    companion object{
        fun request(): ArrayList<FieldDescriptor>{
            val fieldDescriptor = ArrayList<FieldDescriptor>()
            fieldDescriptor.add(PayloadDocumentation.fieldWithPath("Message").description("Message sent").type("String"))
            return fieldDescriptor
        }
    }
}
