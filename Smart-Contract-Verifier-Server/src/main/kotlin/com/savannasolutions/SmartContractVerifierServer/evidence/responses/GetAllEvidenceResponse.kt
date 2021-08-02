package com.savannasolutions.SmartContractVerifierServer.evidence.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus

data class GetAllEvidenceResponse(val responseStatus: ResponseStatus,
                                  var evidenceHashes: List<String>,)
