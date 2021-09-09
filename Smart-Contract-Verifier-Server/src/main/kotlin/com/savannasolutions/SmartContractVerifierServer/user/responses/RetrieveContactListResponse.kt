package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.fasterxml.jackson.annotation.JsonProperty
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ContactListAliasWalletResponse
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

data class RetrieveContactListResponse(@JsonProperty("WalletAndAlias") val WalletAndAlias: List<ContactListAliasWalletResponse>? = null,)
{
    companion object{
        fun response(): ArrayList<FieldDescriptor>
        {
            val fieldDescriptorResponse = ArrayList<FieldDescriptor>()
            fieldDescriptorResponse.add(PayloadDocumentation.subsectionWithPath("ResponseObject.walletAndAlias[]").description("This is a list of wallet addresses and aliases"))
            fieldDescriptorResponse.addAll(ContactListAliasWalletResponse.response("ResponseObject.walletAndAlias[]"))
            return fieldDescriptorResponse
        }
    }
}
