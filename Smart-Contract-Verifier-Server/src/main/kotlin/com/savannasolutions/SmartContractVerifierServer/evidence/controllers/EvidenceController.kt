package com.savannasolutions.SmartContractVerifierServer.evidence.controllers

import com.savannasolutions.SmartContractVerifierServer.evidence.interfaces.EvidenceFileSystem
import com.savannasolutions.SmartContractVerifierServer.evidence.requests.*
import com.savannasolutions.SmartContractVerifierServer.evidence.services.EvidenceService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/evidence")
class EvidenceController constructor(private val evidenceService: EvidenceService,
                                     @Qualifier("evidenceFileSystemJIMFSImplementation") val evidenceFileSystem: EvidenceFileSystem,) {

    @PostMapping("/upload")
    fun uploadEvidence(@RequestBody uploadEvidenceRequest: UploadEvidenceRequest) =
        evidenceService.uploadEvidence(uploadEvidenceRequest)

    @PostMapping("/link")
    fun linkEvidence(@RequestBody linkEvidenceRequest: LinkEvidenceRequest) =
        evidenceService.linkEvidence(linkEvidenceRequest)

    @PostMapping("/fetch")
    fun uploadEvidence(@RequestBody fetchEvidenceRequest: FetchEvidenceRequest) =
        evidenceService.fetchEvidence(fetchEvidenceRequest)

    @PostMapping("/get-agreement-evidence")
    fun uploadEvidence(@RequestBody getAllEvidenceRequest: GetAllEvidenceRequest) =
        evidenceService.getAllEvidence(getAllEvidenceRequest)

    @PostMapping("/remove")
    fun uploadEvidence(@RequestBody removeEvidenceRequest: RemoveEvidenceRequest) =
        evidenceService.removeEvidence(removeEvidenceRequest)

}