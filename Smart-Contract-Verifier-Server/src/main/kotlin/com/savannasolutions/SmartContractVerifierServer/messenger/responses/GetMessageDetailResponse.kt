package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.MessageResponse
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class GetMessageDetailResponse(@JsonProperty("MessageDetails") val messageDetails: MessageResponse?=null)
{
    companion object {
        fun response(): ArrayList<FieldDescriptor> {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(
                PayloadDocumentation.subsectionWithPath("ResponseObject.MessageDetails")
                    .description("These are the details of the messages attached to the agreement").optional()
            )
            fieldDescriptorResponse.addAll(MessageResponse.response("ResponseObject.MessageDetails"))
            return fieldDescriptorResponse
        }
    }
}
