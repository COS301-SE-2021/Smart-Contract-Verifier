package com.savannasolutions.SmartContractVerifierServer.evidence.services

import com.savannasolutions.SmartContractVerifierServer.evidence.requests.*
import com.savannasolutions.SmartContractVerifierServer.evidence.responses.*
import org.springframework.stereotype.Service

@Service
class EvidenceService {
    fun uploadEvidence(uploadEvidenceRequest: UploadEvidenceRequest): UploadEvidenceResponse = UploadEvidenceResponse()

    fun linkEvidence(linkEvidenceRequest: LinkEvidenceRequest): LinkEvidenceResponse = LinkEvidenceResponse()

    fun fetchEvidence(fetchEvidenceRequest: FetchEvidenceRequest): FetchEvidenceResponse = FetchEvidenceResponse()

    fun getAllEvidence(getAllEvidenceRequest: GetAllEvidenceRequest): GetAllEvidenceResponse = GetAllEvidenceResponse()

    fun removeEvidence(removeEvidenceRequest: RemoveEvidenceRequest): RemoveEvidenceResponse = RemoveEvidenceResponse()

}