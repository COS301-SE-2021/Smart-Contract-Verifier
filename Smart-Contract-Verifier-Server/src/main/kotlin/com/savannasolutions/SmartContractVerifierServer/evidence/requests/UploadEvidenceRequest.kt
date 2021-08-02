package com.savannasolutions.SmartContractVerifierServer.evidence.requests

import org.springframework.web.multipart.MultipartFile
import java.util.*

data class UploadEvidenceRequest(val contractId: UUID,
                                 val userId: String,
                                 val fileToUpload: MultipartFile,)
