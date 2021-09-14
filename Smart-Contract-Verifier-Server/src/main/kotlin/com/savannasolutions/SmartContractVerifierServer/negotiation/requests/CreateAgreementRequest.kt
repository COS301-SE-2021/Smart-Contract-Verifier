package com.savannasolutions.SmartContractVerifierServer.negotiation.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class CreateAgreementRequest(@JsonProperty("PartyB") val PartyB:String,
                                  @JsonProperty("AgreementTitle") val Title:String,
                                  @JsonProperty("AgreementDescription") val Description: String,
                                  @JsonProperty("AgreementImageURL") val ImageURL: String,)
{
    companion object{
        fun request(): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorRequests = ArrayList<FieldDescriptor>()
            fieldDescriptorRequests.add(PayloadDocumentation.fieldWithPath("PartyB").description("This is the public wallet of the other party").type("String"))
            fieldDescriptorRequests.add(PayloadDocumentation.fieldWithPath("AgreementTitle").description("This is the title of the agreement").type("String"))
            fieldDescriptorRequests.add(PayloadDocumentation.fieldWithPath("AgreementDescription").description("This is the description of the agreement").type("String"))
            fieldDescriptorRequests.add(PayloadDocumentation.fieldWithPath("AgreementImageURL").description("This is the image url for the agreement").type("String"))
            return fieldDescriptorRequests
        }
    }
}
