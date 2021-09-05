package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.io.FileDescriptor
import java.util.*
import kotlin.collections.ArrayList

data class ConditionResponse(@JsonProperty("ConditionID") val conditionID: UUID,
                             @JsonProperty("ConditionDescription") val conditionDescription: String? = null,
                             @JsonProperty("ProposingUser") val proposingUser : UserResponse? = null,
                             @JsonProperty("ProposalDate") val proposalDate : Date? = null,
                             @JsonProperty("AgreementID") val agreementID : UUID? = null,
                             @JsonProperty("ConditionStatus") val conditionStatus: Enum<ConditionStatus>? = null,
                             @JsonProperty("ConditionTitle") val conditionTitle: String? = null,)
{
    companion object{
        fun response(Path: String): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.ConditionID").description("This is the unique ID for the condition").type("UUID"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.ConditionDescription").description("This is the description for the condition").type("String"))
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("$Path.ProposingUser").description("This is the public wallet address for the user who proposed the condition"))
            fieldDescriptorResponse.addAll(UserResponse.response("$Path.ProposingUser"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.AgreementID").description("This is the unique id for the agreement this condition is attached to").type("UUID"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.ConditionStatus").description("This is the status of the condition. It can be PENDING, ACCEPTED or REJECTED").type("ENUM"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.ConditionTitle").description("This is the title for the condition").type("String"))
            return fieldDescriptorResponse

        }
    }
}
