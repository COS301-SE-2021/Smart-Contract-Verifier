package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ContactListIDContactListNameResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class RetrieveUserContactListResponse(@JsonProperty("ContactListInfo") val ContactListInfo: List<ContactListIDContactListNameResponse>?= null,)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.contactListInfo[]").description("This is the list of contact list information"))
            fieldDescriptorResponse.addAll(ContactListIDContactListNameResponse.response("ResponseObject.contactListInfo[]"))
            return fieldDescriptorResponse
        }

        fun responseEmpty(): ArrayList<FieldDescriptor>{
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.contactListInfo[]").description("This is the list of contact list information"))
            return fieldDescriptorResponse
        }
    }
}
