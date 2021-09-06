package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*
import kotlin.collections.ArrayList

data class ContactListIDContactListNameResponse(@JsonProperty("ContactListName") val contactListName : String,
                                                @JsonProperty("ContactListID") val contactListID: UUID)
{
    companion object{
        fun response(Path: String): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.ContactListName").description("This is the name of the contact list").type("String"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.ContactListID").description("This is the unique ID of the contact list").type("UUID"))
            return fieldDescriptorResponse
        }
    }
}
