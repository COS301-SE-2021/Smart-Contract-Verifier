package com.savannasolutions.SmartContractVerifierServer.negotiation.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.AgreementResponse
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class GetAgreementDetailsResponse(@JsonProperty("AgreementResponse") val agreementResponse : AgreementResponse?=null,)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.AgreementResponse").description("This is the agreement details"))
            fieldDescriptorResponse.addAll(AgreementResponse.response("ResponseObject.AgreementResponse"))
            return fieldDescriptorResponse
        }
    }
}

