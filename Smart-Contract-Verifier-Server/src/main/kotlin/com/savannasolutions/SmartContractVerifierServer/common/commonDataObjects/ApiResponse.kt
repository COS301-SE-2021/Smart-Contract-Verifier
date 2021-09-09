package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    @JsonProperty("Status") val status : ResponseStatus,
    @JsonProperty("ResponseObject") val responseObject : T? = null,
    @JsonProperty("Message") val message : String? = null
) {
    companion object{
        fun apiResponse(): ArrayList<FieldDescriptor>{
            val responseFieldDescriptors = ArrayList<FieldDescriptor>()
            responseFieldDescriptors.add(PayloadDocumentation.fieldWithPath("Status").description("Will return successful or failed").type("ENUM"))
            responseFieldDescriptors.add(PayloadDocumentation.subsectionWithPath("ResponseObject").description("If status is successful this will be a populated response object. If status is failed this will be a null object"))
            return responseFieldDescriptors
        }

        fun apiFailedResponse():ArrayList<FieldDescriptor>{
            val responseFieldDescriptors = ArrayList<FieldDescriptor>()
            responseFieldDescriptors.add(PayloadDocumentation.fieldWithPath("Status").description("Will return successful or failed").type("ENUM"))
            responseFieldDescriptors.add(PayloadDocumentation.fieldWithPath("Message").description("This will contain an error message if needed").type("String"))
            return responseFieldDescriptors
        }

        fun apiEmptyResponse(): ArrayList<FieldDescriptor>{
            val responseFieldDescriptors = ArrayList<FieldDescriptor>()
            responseFieldDescriptors.add(PayloadDocumentation.fieldWithPath("Status").description("Will return successful or failed").type("ENUM"))
            return responseFieldDescriptors
        }
    }
}