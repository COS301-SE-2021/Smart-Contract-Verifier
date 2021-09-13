package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*

data class MessageResponse(@JsonProperty("MessageID") val MessageID: UUID,
                           @JsonProperty("SendingUser") val SendingUser: UserResponse,
                           @JsonProperty("SendingDate") val SendingDate: Date,
                           @JsonProperty("AgreementID") val AgreementID: UUID,
                           @JsonProperty("Message") val Message: String,
                           @JsonProperty("MessageStatuses") val MessageStatuses: List<MessageStatusResponse>)
{
    companion object{
        fun response(Path: String): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponses = ArrayList<FieldDescriptor>()
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.messageID").description("This is the unique id of the message").type("UUID"))
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.sendingUser").description("This is the public wallet address of the user who sent the message").type("String"))
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.sendingDate").description("This is the date the message was sent").type("Java Date"))
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.agreementID").description("This is the unique id of the agreement which the message is in regards with").type("UUID"))
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.message").description("This is the actual message").type("String"))
            fieldDescriptorResponses.add(PayloadDocumentation.subsectionWithPath("$Path.messageStatuses[]").description("These are the statuses of the recipients and the message").optional())
            fieldDescriptorResponses.addAll(MessageStatusResponse.response("$Path.messageStatuses[]"))
            return fieldDescriptorResponses
        }
    }
}
