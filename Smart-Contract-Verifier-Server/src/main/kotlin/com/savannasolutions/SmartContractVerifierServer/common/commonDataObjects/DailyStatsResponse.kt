package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*
import kotlin.collections.ArrayList

data class DailyStatsResponse(@JsonProperty("Date") val date : Date,
                              @JsonProperty("AgreementsCreatedOnDay") val agreementsCreatedOnDay : Int,
                              @JsonProperty("AgreementsCreatedUpUntilDay") val agreementsCreatedUpTillDay : Int,
                              @JsonProperty("SealedAgreementsOnDay") val successfulAgreementsOnDay: Int,
                              @JsonProperty("SealedAgreementsUpUntilDay") val successfulAgreementsUpTillDay: Int,)
{
    companion object{
        fun response(path: String): ArrayList<FieldDescriptor>
        {
            val responseList = ArrayList<FieldDescriptor>()
            responseList.add(PayloadDocumentation.fieldWithPath("${path}.Date").description("This is the date for the provided stats").type("Date"))
            responseList.add(PayloadDocumentation.fieldWithPath("${path}.AgreementsCreatedOnDay").description("This is the amount of agreements that was created on the day").type("Integer"))
            responseList.add(PayloadDocumentation.fieldWithPath("${path}.AgreementsCreatedUpUntilDay").description("This is the amount of agreements made up until day").type("Integer"))
            responseList.add(PayloadDocumentation.fieldWithPath("${path}.SealedAgreementsOnDay").description("This is the amount of agreements made on the day").type("Integer"))
            responseList.add(PayloadDocumentation.fieldWithPath("${path}.SealedAgreementsUpUntilDay").description("This is the amount of agreements sealed up until day").type("Integer"))
            return responseList
        }
    }
}