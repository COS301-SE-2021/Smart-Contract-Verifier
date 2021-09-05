package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import java.util.*
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class MessagesByAgreementResponse(@JsonProperty("AgreementID") val AgreementID: UUID,
                                        @JsonProperty("Messages") val Messages: List<MessageResponse>?= emptyList())
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("ResponseObject.messageAgreementList[].agreementID").description("This is the agreement that all the messages are attached to").type("UUID"))
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.messageAgreementList[].messages[]").description("These are the details of the messages attached to the agreement").optional())
            fieldDescriptorResponse.addAll(MessageResponse.response("ResponseObject.messageAgreementList[].messages[]"))
            return fieldDescriptorResponse
        }

        fun emptyResponse(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("ResponseObject.AgreementID").description("This is the agreement that all the messages are attached to").type("UUID"))
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.messageAgreementList.messages[]").description("These are the details of the messages attached to the agreement").optional())
            return fieldDescriptorResponse
        }
    }
}
