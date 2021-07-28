package com.savannasolutions.SmartContractVerifierServer.negotiation.models

import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.util.*
import javax.persistence.*

@Entity
data class Agreements(@Id @GeneratedValue val ContractID:UUID,
                      var AgreementTitle: String = "Un-named Agreement",
                      var AgreementDescription: String = "",
                      var blockchainID: String? = null,
                      val CreatedDate:Date,
                      var SealedDate:Date? = null,
                      var DurationConditionUUID: UUID? = null,
                      var MovedToBlockChain:Boolean,
                      var PaymentConditionUUID: UUID? = null,
                      var AgreementImageURL: String? = null)
{
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract", orphanRemoval = true, cascade = [CascadeType.ALL])
    var conditions: List<Conditions>? = emptyList()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agreements", orphanRemoval = true, cascade = [CascadeType.ALL])
    var messages: List<Messages>? = emptyList()

    @ManyToMany(mappedBy = "agreements", cascade = [CascadeType.ALL])
    val users: MutableSet<User> = mutableSetOf()
}
