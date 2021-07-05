package com.savannasolutions.SmartContractVerifierServer.user.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements

data class RetrieveUserAgreementsResponse(val AgreementIDs: List<Agreements>? = null,
                                            val status: ResponseStatus,)
