package com.savannasolutions.SmartContractVerifierServer.evidence.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.EvidenceDetailsResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.LinkedEvidenceDetailsResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.UploadedEvidenceDetailsResponse
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class GetAllEvidenceResponse(@JsonProperty("UploadedEvidenceDetails") val uploadedEvidenceDetails : List<EvidenceDetailsResponse<UploadedEvidenceDetailsResponse?>>? = emptyList(),
                                  @JsonProperty("LinkedEvidenceDetails") val linkedEvidenceDetails : List<EvidenceDetailsResponse<LinkedEvidenceDetailsResponse?>>? = emptyList())
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("ResponseObject.UploadedEvidenceDetails[]").description("This is the details of the uploaded evidence"))
            fieldDescriptorResponse.addAll(EvidenceDetailsResponse.response("ResponseObject.UploadedEvidenceDetails[]"))
            fieldDescriptorResponse.addAll(UploadedEvidenceDetailsResponse.response("ResponseObject.UploadedEvidenceDetails[].EvidenceSpecificDetail"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("ResponseObject.LinkedEvidenceDetails[]").description("This is the details of the linked evidence"))
            fieldDescriptorResponse.addAll(EvidenceDetailsResponse.response("ResponseObject.LinkedEvidenceDetails[]"))
            fieldDescriptorResponse.addAll(LinkedEvidenceDetailsResponse.response("ResponseObject.LinkedEvidenceDetails[].EvidenceSpecificDetail"))
            return fieldDescriptorResponse
        }
    }
}
