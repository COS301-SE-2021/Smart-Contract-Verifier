package com.savannasolutions.SmartContractVerifierServer.evidence.responses

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import org.springframework.web.multipart.MultipartFile

data class FetchEvidenceResponse(val responseStatus: ResponseStatus,
                                 val evidenceUrl: String?,
                                 val evidence: MultipartFile?,)
