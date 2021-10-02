package com.savannasolutions.SmartContractVerifierServer.stats.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.time.LocalDate

data class DetailedRequest(@JsonProperty("StartDate") val startDate: LocalDate, @JsonProperty("EndDate") val endDate: LocalDate){
    companion object{
        fun request(): ArrayList<FieldDescriptor>
        {
            val requestList = ArrayList<FieldDescriptor>()
            requestList.add(PayloadDocumentation.fieldWithPath("StartDate").description("This is the start date of the query").type("LocalDate"))
            requestList.add(PayloadDocumentation.fieldWithPath("EndDate").description("This is the end date of the query").type("LocalDate"))
            return requestList
        }
    }
}
