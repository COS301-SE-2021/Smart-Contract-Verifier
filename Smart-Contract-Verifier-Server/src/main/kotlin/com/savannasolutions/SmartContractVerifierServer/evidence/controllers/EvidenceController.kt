package com.savannasolutions.SmartContractVerifierServer.evidence.controllers

import com.savannasolutions.SmartContractVerifierServer.evidence.interfaces.EvidenceFileSystem
import com.savannasolutions.SmartContractVerifierServer.evidence.services.EvidenceService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/evidence")
class EvidenceController constructor(private val evidenceService: EvidenceService,
                                     @Qualifier("evidenceFileSystemJIMFSImplementation") val evidenceFileSystem: EvidenceFileSystem,) {

}