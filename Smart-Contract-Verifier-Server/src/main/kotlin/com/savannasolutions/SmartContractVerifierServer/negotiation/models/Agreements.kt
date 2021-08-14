package com.savannasolutions.SmartContractVerifierServer.negotiation.models

import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.contracts.models.Judges
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import java.math.BigInteger
import java.util.*
import javax.persistence.*

@Entity
data class Agreements(@Id @GeneratedValue val ContractID:UUID,
                      var AgreementTitle: String = "Un-named Agreement",
                      var AgreementDescription: String = "",
                      var blockchainID: BigInteger? = null,
                      val CreatedDate:Date,
                      var SealedDate:Date? = null,
                      var DurationConditionUUID: UUID? = null,
                      var MovedToBlockChain:Boolean = false,
                      var PaymentConditionUUID: UUID? = null,
                      var AgreementImageURL: String? = null)
{
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract", orphanRemoval = true, cascade = [CascadeType.PERSIST])
    var conditions: List<Conditions>? = emptyList()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agreements", orphanRemoval = true, cascade = [CascadeType.ALL])
    var messages: List<Messages>? = emptyList()

    @ManyToMany(mappedBy = "agreements", cascade = [CascadeType.PERSIST])
    val users: MutableSet<User> = mutableSetOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agreement", orphanRemoval = true, cascade = [CascadeType.ALL])
    var judges: List<Judges>?= emptyList()
}
