package com.savannasolutions.SmartContractVerifierServer.evidence.controllers

import com.google.common.net.MediaType
import com.savannasolutions.SmartContractVerifierServer.evidence.requests.*
import com.savannasolutions.SmartContractVerifierServer.evidence.services.EvidenceService
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
class EvidenceController constructor(private val evidenceService: EvidenceService,) {

    @PostMapping("/user/{userId}/agreement/{agreementId}/evidence/upload", consumes = [org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadEvidence(@PathVariable userId: String,
                       @PathVariable agreementId: UUID,
                       @RequestParam uploadEvidence: MultipartFile,) =
        evidenceService.uploadEvidence(userId, agreementId, uploadEvidence)

    @PostMapping("/user/{userId}/agreement/{agreementId}/evidence/link")
    fun linkEvidence(@PathVariable userId: String,
                     @PathVariable agreementId: UUID,
                     @RequestBody linkEvidenceRequest: LinkEvidenceRequest,) =
        evidenceService.linkEvidence(userId, agreementId, linkEvidenceRequest)

    @GetMapping("/user/{userId}/agreement/{agreementId}/evidence/{evidenceHash}/linked")
    fun retrieveEvidence(@PathVariable userId: String,
                         @PathVariable agreementId: UUID,
                         @PathVariable evidenceHash: String,) =
        evidenceService.fetchEvidence(userId, agreementId, evidenceHash)

    @GetMapping("/user/{userId}/agreement/{agreementId}/evidence/{evidenceHash}/download")
    fun downloadEvidence(@PathVariable userId: String, @PathVariable agreementId: UUID, @PathVariable evidenceHash: String,){

    }

    @GetMapping("/user/{userId}/agreement/{agreementId}/evidence/")
    fun getAllEvidence(@PathVariable userId: String,
                       @PathVariable agreementId: UUID,) =
        evidenceService.getAllEvidence(userId, agreementId)

    @DeleteMapping("/user/{userId}/agreement/{agreementId}/evidence/{evidenceHash}")
    fun removeEvidence(@PathVariable userId: String,
                       @PathVariable agreementId: UUID,
                       @PathVariable evidenceHash: String,) =
        evidenceService.removeEvidence(userId, agreementId, evidenceHash)

}