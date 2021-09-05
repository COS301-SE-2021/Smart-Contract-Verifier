package com.savannasolutions.SmartContractVerifierServer.messenger.responses

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.MessageResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.MessagesByAgreementResponse
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class GetUnreadMessagesResponse(val MessageAgreementList :List<MessagesByAgreementResponse>?= emptyList())
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.messageAgreementList[]").description("These are the details of the messages attached to the agreement").optional())
            fieldDescriptorResponse.addAll(MessagesByAgreementResponse.response())
            return fieldDescriptorResponse
        }

        fun emptyResponse(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.messageAgreementList[]").description("These are the details of the messages attached to the agreement").optional())
            return fieldDescriptorResponse
        }
    }
}

