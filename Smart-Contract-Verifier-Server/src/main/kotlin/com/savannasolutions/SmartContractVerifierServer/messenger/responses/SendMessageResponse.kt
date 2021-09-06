package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*

data class SendMessageResponse(@JsonProperty("MessageID") val MessageID: UUID? = null,
){
    companion object{
        fun response(): ArrayList<FieldDescriptor>
        {
            val fieldDescriptor = ArrayList<FieldDescriptor>()
            fieldDescriptor.add(PayloadDocumentation.fieldWithPath("ResponseObject.MessageID").description("This is the unique ID of this message").type("UUID").optional())
            return fieldDescriptor
        }
    }
}
