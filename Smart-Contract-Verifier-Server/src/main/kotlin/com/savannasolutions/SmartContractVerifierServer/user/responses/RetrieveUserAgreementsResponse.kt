package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.io.FileDescriptor

data class RetrieveUserAgreementsResponse(@JsonProperty("Agreements") val Agreements: List<AgreementResponse>? = null,)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.agreements[]").description("This is the list of agreements the user currently has"))
            fieldDescriptorResponse.addAll(AgreementResponse.response("ResponseObject.agreements[]"))
            return fieldDescriptorResponse
        }

        fun responseDurationPaymentEmpty(): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.agreements[]").description("This is the list of agreements the user currently has"))
            fieldDescriptorResponse.addAll(AgreementResponse.responseDurationPaymentEmpty("ResponseObject.agreements[]"))
            return fieldDescriptorResponse
        }
    }
}
