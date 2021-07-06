package com.savannasolutions.SmartContractVerifierServer.responses

import com.savannasolutions.SmartContractVerifierServer.models.Conditions
import java.time.Duration
import java.util.*

data class GetAgreementDetailsResponse(val agreementID: UUID,
                                        val durationID: UUID? = null,
                                        val paymentID: UUID? = null,
                                        val partyA: String? = null,
                                        val partyB: String? = null,
                                        val createdDate: Date? = null,
                                        val sealedDate: Date? = null,
                                        val movedToBlockchain: Boolean? = null,
                                        val conditions: List<UUID>? = null,
                                        val status: Enum<ResponseStatus>,)
