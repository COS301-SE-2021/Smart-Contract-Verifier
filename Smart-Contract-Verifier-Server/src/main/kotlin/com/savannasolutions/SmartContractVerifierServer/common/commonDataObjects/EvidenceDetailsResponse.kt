package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EvidenceDetailsResponse<T>(@JsonProperty("EvidenceHash") val evidenceHash: String,
                                        @JsonProperty("EvidenceID") val evidenceID: UUID,
                                        @JsonProperty("EvidenceType") val evidenceType: EvidenceType,
                                        @JsonProperty("EvidenceSpecificDetail") val evidenceSpecificDetail: T?,
                                        @JsonProperty("EvidenceOwner") val userResponse: UserResponse,
                                        @JsonProperty("Removed") val removed: Boolean) {
    companion object{
        fun response(Path: String): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.EvidenceHash").description("This is the hash for this evidence").type("String"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.EvidenceID").description("This is the unique id for the evidence").type("UUID"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.EvidenceType").description("This is the type of evidence").type("Enum<EvidenceType>"))
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("$Path.EvidenceSpecificDetail").description("This is the evidence specific details").optional())
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("$Path.EvidenceOwner").description("This is the owner of the evidence"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.Removed").description("This indicates if the evidence has been removed"))
            return fieldDescriptorResponse
        }
    }
}