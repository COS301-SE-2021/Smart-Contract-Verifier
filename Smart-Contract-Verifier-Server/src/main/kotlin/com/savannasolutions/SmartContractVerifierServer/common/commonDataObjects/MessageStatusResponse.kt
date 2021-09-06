package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*

data class MessageStatusResponse(@JsonProperty("RecipientID") val RecipientID: String,
                                 @JsonProperty("Read") val Read: Boolean,
                                 @JsonProperty("ReadDate") val ReadDate: Date?)
{
    companion object{
        fun response(Path: String): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.recipientID").description("This is the public wallet address of the recipient for the message").type("String"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.read").description("This is a flag indicating if the message has been read").type("Boolean"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.readDate").description("This is the date that the message was read by the participant").type("Java date").optional())
            return fieldDescriptorResponse
        }
    }
}
