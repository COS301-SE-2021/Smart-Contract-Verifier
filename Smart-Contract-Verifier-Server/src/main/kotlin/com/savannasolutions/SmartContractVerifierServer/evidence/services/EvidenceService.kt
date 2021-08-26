package com.savannasolutions.SmartContractVerifierServer.evidence.services

import com.savannasolutions.SmartContractVerifierServer.evidence.interfaces.EvidenceFileSystem
import com.savannasolutions.SmartContractVerifierServer.evidence.requests.*
import com.savannasolutions.SmartContractVerifierServer.evidence.responses.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class EvidenceService constructor(@Qualifier("evidenceFileSystemJIMFSImplementation") val evidenceFileSystem: EvidenceFileSystem,) {
    fun uploadEvidence(uploadEvidenceRequest: UploadEvidenceRequest): UploadEvidenceResponse? = null

    fun linkEvidence(linkEvidenceRequest: LinkEvidenceRequest): LinkEvidenceResponse? = null

    fun fetchEvidence(): FetchEvidenceResponse? = null

    fun getAllEvidence(): GetAllEvidenceResponse? = null

    fun removeEvidence(): RemoveEvidenceResponse? = null

}