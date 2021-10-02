package com.savannasolutions.SmartContractVerifierServer.stats.responses

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class GeneralStatsResponse(@JsonProperty("TotalUsers") val totalUser : Int,
                                @JsonProperty("TotalAgreements") val totalAgreements : Int,
                                @JsonProperty("NumberOfJudges") val numberOfJudges: Int,
                                @JsonProperty("SealedAgreements") val sealedAgreements: Int,
                                @JsonProperty("UnSealedAgreements") val unsealedAgreements: Int,
                                @JsonProperty("AverageNegotiationPeriod") val averageNegotiationPeriod: Double,
                                @JsonProperty("ConcludedAgreements") val concludedAgreements: Int,
                                @JsonProperty("DisputedAgreements") val disputedAgreements: Int) {
    companion object{
        fun response() : ArrayList<FieldDescriptor>
        {
            val responseList = ArrayList<FieldDescriptor>()
            responseList.add(PayloadDocumentation.fieldWithPath("ResponseObject.TotalUsers").description("This is the total amount of users in the system").type("Integer"))
            responseList.add(PayloadDocumentation.fieldWithPath("ResponseObject.TotalAgreements").description("This is the total amount of agreements in the system").type("Integer"))
            responseList.add(PayloadDocumentation.fieldWithPath("ResponseObject.NumberOfJudges").description("This is the total number of judges").type("Integer"))
            responseList.add(PayloadDocumentation.fieldWithPath("ResponseObject.SealedAgreements").description("This is the total amount of sealed agreements").type("Integer"))
            responseList.add(PayloadDocumentation.fieldWithPath("ResponseObject.UnSealedAgreements").description("This is the total amount of unsealed agreements").type("Integer"))
            responseList.add(PayloadDocumentation.fieldWithPath("ResponseObject.AverageNegotiationPeriod").description("This is the average time an agreement is in negotiation").description("Double"))
            responseList.add(PayloadDocumentation.fieldWithPath("ResponseObject.ConcludedAgreements").description("This is the total amount of concluded agreements").type("Integer"))
            responseList.add(PayloadDocumentation.fieldWithPath("ResponseObject.DisputedAgreements").description("This is the total amount of disputed agreements").type("Integer"))
            return responseList
        }
    }
}