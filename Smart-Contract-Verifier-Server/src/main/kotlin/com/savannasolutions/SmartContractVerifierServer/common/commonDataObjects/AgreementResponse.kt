package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList

data class AgreementResponse(@JsonProperty("AgreementID") val agreementID: UUID,
                             @JsonProperty("AgreementTitle") val agreementTitle: String,
                             @JsonProperty("AgreementDescription") val agreementDescription: String,
                             @JsonProperty("DurationCondition") val durationCondition: DurationConditionResponse? = null,
                             @JsonProperty("PaymentCondition") val paymentCondition: PaymentConditionResponse? = null,
                             @JsonProperty("PartyA") val partyA: UserResponse? = null,
                             @JsonProperty("PartyB") val partyB: UserResponse? = null,
                             @JsonProperty("CreatedDate") val createdDate: Date? = null,
                             @JsonProperty("SealedDate") val sealedDate: Date? = null,
                             @JsonProperty("MovedToBlockchain") val movedToBlockchain: Boolean? = false,
                             @JsonProperty("Conditions") val conditions: List<ConditionResponse>? = emptyList(),
                             @JsonProperty("AgreementImageURL") val agreementImageURL: String? = null,
                             @JsonProperty("BlockChainID") val blockChainID: BigInteger? = null,)
{
    companion object{
        fun response(Path: String): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponses = ArrayList<FieldDescriptor>()
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.AgreementID").description("This is the unique ID of the agreement").type("UUID"))
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.AgreementTitle").description("This is the title of the agreement").type("String"))
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.AgreementDescription").description("This is the description of the agreement").type("String"))
            fieldDescriptorResponses.add(PayloadDocumentation.subsectionWithPath("$Path.DurationCondition").description("This is the current duration condition, and may be null").optional())
            fieldDescriptorResponses.addAll(DurationConditionResponse.response("$Path.DurationCondition"))
            fieldDescriptorResponses.add(PayloadDocumentation.subsectionWithPath("$Path.PaymentCondition").description("This is the current payment condition, and may be null").optional())
            fieldDescriptorResponses.addAll(PaymentConditionResponse.response("$Path.PaymentCondition"))
            fieldDescriptorResponses.add(PayloadDocumentation.subsectionWithPath("$Path.PartyA").description("This is the public wallet id of party A"))
            fieldDescriptorResponses.addAll(UserResponse.response("$Path.PartyA"))
            fieldDescriptorResponses.add(PayloadDocumentation.subsectionWithPath("$Path.PartyB").description("This is the public wallet id of party B"))
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.CreatedDate").description("This is the date that the agreement was initially proposed").type("Date").optional())
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.SealedDate").description("This is the date that the agreement was sealed on the backend").type("Date").optional())
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.MovedToBlockChain").description("This flag indicates if the agreement has been moved to the blockchain").type("Boolean").optional())
            fieldDescriptorResponses.add(PayloadDocumentation.subsectionWithPath("$Path.Conditions[]").description("This is the possibly empty list of conditions for the agreement"))
            fieldDescriptorResponses.addAll(ConditionResponse.response("$Path.Conditions[]"))
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.AgreementImageURL").description("This is the url to the image associated with the agreement").type("String").optional())
            fieldDescriptorResponses.add(PayloadDocumentation.fieldWithPath("$Path.BlockchainID").description("This is the blockchain id for when the agreement has been moved to the blockchain").type("Long Int").optional())
            return fieldDescriptorResponses
        }
    }
}
