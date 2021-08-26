package com.savannasolutions.SmartContractVerifierServer.evidence.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.multipart.MultipartFile
import java.util.*

data class UploadEvidenceRequest(@JsonProperty("uploadFile") val fileToUpload: MultipartFile,)
