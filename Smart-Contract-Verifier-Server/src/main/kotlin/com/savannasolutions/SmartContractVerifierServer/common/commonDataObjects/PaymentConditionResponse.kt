package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*

data class PaymentConditionResponse(@JsonProperty("ID") val conditionID: UUID,
                                    @JsonProperty("Amount") val amount: Double,
                                    @JsonProperty("Payer") val payerWalletID: String,
                                    @JsonProperty("ConditionStatus") val conditionStatus: Enum<ConditionStatus>,)
{
    companion object{
        fun response(Path : String): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.id").description("This is the unique id of the condition").type("UUID"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.amount").description("This is the amount of unison tokens the agreement was contracted for").type("Double"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.conditionStatus").description("This is the status of the condition. It can either be PENDING, REJECTED or ACCEPTED").type("ENUM"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.payer").description("This is the public wallet ID of the person responsible for paying").type("String"))
            return fieldDescriptorResponse
        }
    }
}
