package com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import java.util.*
import kotlin.collections.ArrayList

data class ContactListAliasWalletResponse(@JsonProperty("Alias") val Alias : String,
                                          @JsonProperty("WalletID") val WalletID:String)
{
    companion object{
        fun response(Path: String):ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.alias").description("This is the alias for this public wallet address in the contact list"))
            fieldDescriptorResponse.add(PayloadDocumentation.fieldWithPath("$Path.walletID").description("This is the public wallet address for the user in the contact list"))
            return fieldDescriptorResponse
        }
    }
}
