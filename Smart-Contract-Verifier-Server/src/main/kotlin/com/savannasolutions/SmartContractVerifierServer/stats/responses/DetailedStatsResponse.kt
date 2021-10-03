package com.savannasolutions.SmartContractVerifierServer.stats.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.DailyStatsResponse
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class DetailedStatsResponse(@JsonProperty("DailyStatsList") val dailyStatsList : List<DailyStatsResponse>,
                                @JsonProperty("AgreementsCreatedBetweenDates") val agreementsCreatedBetweenDates: Int,
                                @JsonProperty("AgreementsSealedBetweenDates") val agreementsSealedBetweenDates: Int){
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val responseList = ArrayList<FieldDescriptor>()
            responseList.add(PayloadDocumentation.subsectionWithPath("ResponseObject.DailyStatsList[]").description("This is a list of daily stats"))
            responseList.addAll(DailyStatsResponse.response("ResponseObject.DailyStatsList[]"))
            responseList.add(PayloadDocumentation.fieldWithPath("ResponseObject.AgreementsCreatedBetweenDates").description("This is the amount of agreements created between the two dates").type("Integer"))
            responseList.add(PayloadDocumentation.fieldWithPath("ResponseObject.AgreementsSealedBetweenDates").description("This is the amount of agreements sealed between the two dates").type("Integer"))
            return responseList
        }
    }
}
