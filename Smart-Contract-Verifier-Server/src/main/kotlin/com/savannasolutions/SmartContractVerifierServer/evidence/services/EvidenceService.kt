package com.savannasolutions.SmartContractVerifierServer.evidence.services

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.interfaces.EvidenceFileSystem
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.LinkedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.requests.*
import com.savannasolutions.SmartContractVerifierServer.evidence.responses.*
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

@Service
@EnableConfigurationProperties(EvidenceConfig::class)
class EvidenceService constructor(val agreementsRepository: AgreementsRepository,
                                  val userRepository: UserRepository,
                                  val evidenceRepository: EvidenceRepository,
                                  val linkedEvidenceRepository: LinkedEvidenceRepository,
                                  val uploadedEvidenceRepository: UploadedEvidenceRepository,
                                  val evidenceConfig: EvidenceConfig,) {
    lateinit var fileSystem: EvidenceFileSystem

    @PostConstruct
    fun initialise() {
        fileSystem = evidenceConfig.filesystem
    }

    fun uploadEvidence(userId: String, agreementId: UUID, uploadEvidenceRequest: UploadEvidenceRequest): UploadEvidenceResponse? = null

    fun linkEvidence(userId: String, agreementId: UUID, linkEvidenceRequest: LinkEvidenceRequest): LinkEvidenceResponse {
        if(!agreementsRepository.existsById(agreementId))
            return LinkEvidenceResponse(ResponseStatus.FAILED)

        val agreement = agreementsRepository.getById(agreementId)

        if(!userRepository.existsById(userId))
            return LinkEvidenceResponse(ResponseStatus.FAILED)

        val user = userRepository.getById(userId)

        if(!agreement.users.contains(user))
            return LinkEvidenceResponse(ResponseStatus.FAILED)

        val nEvidence = Evidence("", EvidenceType.LINKED)
        val linkedEvidence = LinkedEvidence(UUID.fromString("6612469d-ffd8-4126-8c5b-9e5873aaf8f3"),
                                            linkEvidenceRequest.url,)
        nEvidence.evidenceUrl = linkedEvidence

        evidenceRepository.save(nEvidence)
        linkedEvidenceRepository.save(linkedEvidence)

        return LinkEvidenceResponse(ResponseStatus.SUCCESSFUL)
    }

    fun fetchEvidence(userId: String, agreementId: UUID, evidenceHash: String): FetchEvidenceResponse? = null

    fun getAllEvidence(userId: String, agreementId: UUID): GetAllEvidenceResponse? = null

    fun removeEvidence(userId: String, agreementId: UUID, evidenceHash: String): RemoveEvidenceResponse {
        //the agreement doesn't exist
        if(!agreementsRepository.existsById(agreementId))
            return RemoveEvidenceResponse(ResponseStatus.FAILED)
        val agreement = agreementsRepository.getById(agreementId)

        //the user doesn't exist
        if(!userRepository.existsById(userId))
            return RemoveEvidenceResponse(ResponseStatus.FAILED)
        val user = userRepository.getById(userId)

        //the user isn't party to the agreement
        if(!agreement.users.contains(user))
            return  RemoveEvidenceResponse(ResponseStatus.FAILED)

        //evidence doesn't exist
        if(!evidenceRepository.existsById(evidenceHash))
            return RemoveEvidenceResponse(ResponseStatus.FAILED)
        val evidence = evidenceRepository.getById(evidenceHash)

        //user doesn't own the file
        if(evidence.user != user)
            return RemoveEvidenceResponse(ResponseStatus.FAILED)

        // delete actual file
        val uploadedEvidence = evidence.uploadedEvidence
        if (uploadedEvidence != null) {
            fileSystem.deleteFile(uploadedEvidence.filename)
        }

        //remove link to evidence
        val linkedEvidence = evidence.evidenceUrl
        if(linkedEvidence != null){
            linkedEvidence.evidenceUrl = "Removed"
            linkedEvidenceRepository.save(linkedEvidence)
        }

        //mark as removed
        evidence.removed = true
        evidenceRepository.save(evidence)

        return RemoveEvidenceResponse(ResponseStatus.SUCCESSFUL)
    }

}