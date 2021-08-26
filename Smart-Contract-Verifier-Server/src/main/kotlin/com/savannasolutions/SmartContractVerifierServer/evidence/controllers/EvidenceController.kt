package com.savannasolutions.SmartContractVerifierServer.evidence.controllers

import com.savannasolutions.SmartContractVerifierServer.evidence.interfaces.EvidenceFileSystem
import com.savannasolutions.SmartContractVerifierServer.evidence.requests.*
import com.savannasolutions.SmartContractVerifierServer.evidence.services.EvidenceService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class EvidenceController constructor(private val evidenceService: EvidenceService,) {

    @PostMapping("/user/{userId}/agreement/{agreementId}/evidence/upload")
    fun uploadEvidence(@PathVariable userId: String,
                       @PathVariable agreementId: UUID,
                       @RequestBody uploadEvidenceRequest: UploadEvidenceRequest,) =
        evidenceService.uploadEvidence(userId, agreementId, uploadEvidenceRequest)

    @PostMapping("/user/{userId}/agreement/{agreementId}/evidence/link")
    fun linkEvidence(@PathVariable userId: String,
                     @PathVariable agreementId: UUID,
                     @RequestBody linkEvidenceRequest: LinkEvidenceRequest,) =
        evidenceService.linkEvidence(userId, agreementId, linkEvidenceRequest)

    @GetMapping("/user/{userId}/agreement/{agreementId}/evidence/{evidenceHash}")
    fun retrieveEvidence(@PathVariable userId: String,
                         @PathVariable agreementId: UUID,
                         @PathVariable evidenceHash: String,) =
        evidenceService.fetchEvidence(userId, agreementId, evidenceHash)

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