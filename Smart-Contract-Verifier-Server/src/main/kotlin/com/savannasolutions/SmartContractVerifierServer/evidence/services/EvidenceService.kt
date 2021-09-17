package com.savannasolutions.SmartContractVerifierServer.evidence.services

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.*
import com.savannasolutions.SmartContractVerifierServer.common.responseErrorMessages.commonResponseErrorMessages
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.interfaces.EvidenceFileSystem
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.LinkedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.UploadedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.requests.LinkEvidenceRequest
import com.savannasolutions.SmartContractVerifierServer.evidence.responses.*
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.annotation.PostConstruct

@Service
@AutoConfigureAfter(EvidenceConfig::class)
@EnableConfigurationProperties(EvidenceConfig::class)
class EvidenceService constructor(val agreementsRepository: AgreementsRepository,
                                  val userRepository: UserRepository,
                                  val evidenceRepository: EvidenceRepository,
                                  val linkedEvidenceRepository: LinkedEvidenceRepository,
                                  val uploadedEvidenceRepository: UploadedEvidenceRepository,
                                  val judgesRepository: JudgesRepository,
                                  val evidenceConfig: EvidenceConfig,) {
    lateinit var fileSystem: EvidenceFileSystem

    @PostConstruct
    fun initialise() {
        fileSystem = evidenceConfig.filesystem
    }

    fun uploadEvidence(userId: String, agreementId: UUID, uploadEvidence: MultipartFile): ApiResponse<UploadEvidenceResponse> {
        //agreement doesn't exist
        if(!agreementsRepository.existsById(agreementId))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.agreementDoesNotExist)
        val agreement = agreementsRepository.getById(agreementId)

        //user doesn't exist
        if(!userRepository.existsById(userId))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.userDoesNotExist)
        val user = userRepository.getById(userId)

        //user isn't party to the agreement
        val users = userRepository.getUsersByAgreementsContaining(agreement)
        if (!users.contains(user))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.userNotPartOfAgreement)

        val hashString = computeHash(uploadEvidence)
        var nEvidence = Evidence(UUID.randomUUID(), hashString, EvidenceType.UPLOADED)
        val filename = agreement.ContractID.toString() + user.publicWalletID + uploadEvidence.originalFilename

        //TODO: Check filetype for risk (part of security)
        var nUploadedEvidence = if(uploadEvidence.contentType != null)
            UploadedEvidence(UUID.fromString("6612469d-ffd8-4126-8c5b-9e5873aaf8f3"),
                filename, uploadEvidence.contentType!!, uploadEvidence.originalFilename!!)
        else
            return ApiResponse(status = ResponseStatus.FAILED, message = "File Mime type not provided")

        nEvidence.user = user
        nEvidence.contract = agreement
        nEvidence.uploadedEvidence = nUploadedEvidence
        nEvidence = evidenceRepository.save(nEvidence)
        nUploadedEvidence.evidence = nEvidence
        uploadedEvidenceRepository.save(nUploadedEvidence)
        nEvidence = evidenceRepository.save(nEvidence)

        fileSystem.saveFile(uploadEvidence, filename)

        return ApiResponse(status = ResponseStatus.SUCCESSFUL, UploadEvidenceResponse(evidenceID = nEvidence.evidenceId))
    }

    fun linkEvidence(userId: String, agreementId: UUID, linkEvidenceRequest: LinkEvidenceRequest): ApiResponse<LinkEvidenceResponse> {
        if(!agreementsRepository.existsById(agreementId))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.agreementDoesNotExist)

        val agreement = agreementsRepository.getById(agreementId)

        if(!userRepository.existsById(userId))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.userDoesNotExist)

        val user = userRepository.getById(userId)

        val userList = userRepository.getUsersByAgreementsContaining(agreement)
        if(!userList.contains(user))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.userNotPartOfAgreement)

        val hash = userId+"_"+System.currentTimeMillis().toString()

        var nEvidence = Evidence(UUID.randomUUID(), hash , EvidenceType.LINKED)
        val linkedEvidence = LinkedEvidence(UUID.fromString("6612469d-ffd8-4126-8c5b-9e5873aaf8f3"),
                                            linkEvidenceRequest.url,)
        nEvidence.evidenceUrl = linkedEvidence
        nEvidence.user = user
        nEvidence.contract = agreement
        linkedEvidence.evidence = nEvidence

        nEvidence = evidenceRepository.save(nEvidence)


        return ApiResponse(status = ResponseStatus.SUCCESSFUL, responseObject = LinkEvidenceResponse(nEvidence.evidenceId))
    }

    fun fetchEvidence(userId: String, agreementId: UUID, evidenceId: String): ApiResponse<FetchEvidenceResponse> {
        if(!agreementsRepository.existsById(agreementId))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.agreementDoesNotExist)
        val agreement = agreementsRepository.getById(agreementId)

        //user doesn't exist
        if(!userRepository.existsById(userId))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.userDoesNotExist)
        val user = userRepository.getById(userId)

        //evidence doesn't exist
        if(!evidenceRepository.existsById(UUID.fromString(evidenceId)))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.evidenceDoesNotExist)
        val evidence = evidenceRepository.getById(UUID.fromString(evidenceId))

        //user isn't party to the agreement
        var valid = false
        val judges = agreement.judges
        val userList = userRepository.getUsersByAgreementsContaining(agreement)
        if (!userList.contains(user)) {
            if (judges != null) {
                for(judge in judges){
                    if(judge.judge == user){
                        valid = true
                    }
                }
            }
            if(!valid)
                return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.userNotPartOfAgreement)
        }

        val evidenceLink = evidence.evidenceUrl
        if (evidenceLink != null) {
            return ApiResponse(status = ResponseStatus.SUCCESSFUL,
                                responseObject = FetchEvidenceResponse(evidenceLink.evidenceUrl))
        }
        return ApiResponse(status = ResponseStatus.FAILED, message = "Evidence has been removed")
    }

    fun downloadEvidence(userId: String, agreementId: UUID, evidenceId: String): DownloadEvidenceResponse {
        if(!agreementsRepository.existsById(agreementId))
            return DownloadEvidenceResponse(null, "")
        val agreement = agreementsRepository.getById(agreementId)

        //user doesn't exist
        if(!userRepository.existsById(userId))
            return DownloadEvidenceResponse(null, "")
        val user = userRepository.getById(userId)

        //evidence doesn't exist
        if(!evidenceRepository.existsById(UUID.fromString(evidenceId)))
            return DownloadEvidenceResponse(null, "")
        val evidence = evidenceRepository.getById(UUID.fromString(evidenceId))

        //user isn't party to the agreement
        var valid = false
        val judges = agreement.judges
        val userList = userRepository.getUsersByAgreementsContaining(agreement)
        if (!userList.contains(user)) {
            if (judges != null) {
                for(judge in judges){
                    if(judge.judge == user){
                        valid = true
                    }
                }
            }
            if(!valid)
                return DownloadEvidenceResponse(null, "")
        }

        //return file
        val uploadedEvidence = evidence.uploadedEvidence
        if (uploadedEvidence != null) {
            return DownloadEvidenceResponse(fileSystem.retrieveFile(uploadedEvidence.filename),
                uploadedEvidence.fileMimeType)
        }
        return DownloadEvidenceResponse(null, "")
    }

    fun getAllEvidence(userId: String, agreementId: UUID): ApiResponse<GetAllEvidenceResponse> {
        //agreement doesn't exist
        if(!agreementsRepository.existsById(agreementId))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.agreementDoesNotExist)
        val agreement = agreementsRepository.getById(agreementId)

        //user doesn't exist
        if(!userRepository.existsById(userId))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.userDoesNotExist)
        val user = userRepository.getById(userId)

        //user isn't party to the agreement
        var valid = false
        val judges = agreement.judges
        val userList = userRepository.getUsersByAgreementsContaining(agreement)
        if (!userList.contains(user)) {
            if (judges != null) {
                for(judge in judges){
                    if(judge.judge == user){
                        valid = true
                    }
                }
            }
            if(!valid)
                return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.userNotPartOfAgreement)
        }

        //build list of evidenceHashes
        val evidenceList = evidenceRepository.getAllByContract(agreement)
        val uploadedEvidence = ArrayList<EvidenceDetailsResponse<UploadedEvidenceDetailsResponse?>>()
        val linkedEvidence = ArrayList<EvidenceDetailsResponse<LinkedEvidenceDetailsResponse?>>()

        for(evidence in evidenceList)
        {
            if(evidence.evidenceType == EvidenceType.UPLOADED)
            {
                var uploadedEvidenceDetails : UploadedEvidenceDetailsResponse? = null
                if(evidence.uploadedEvidence!= null) {
                    uploadedEvidenceDetails =
                        UploadedEvidenceDetailsResponse(evidence.uploadedEvidence!!.originalFilename,
                                                        evidence.uploadedEvidence!!.fileMimeType)
                }
                uploadedEvidence.add(EvidenceDetailsResponse(evidence.evidenceHash,
                    evidence.evidenceId,
                    EvidenceType.UPLOADED,
                    uploadedEvidenceDetails,
                    UserResponse( evidence.user.publicWalletID),
                    evidence.removed)
                )

            } else {
                var linkedEvidenceDetails : LinkedEvidenceDetailsResponse? = null
                if(evidence.evidenceUrl != null)
                {
                    linkedEvidenceDetails = LinkedEvidenceDetailsResponse(evidence.evidenceUrl!!.evidenceUrl)
                }
                linkedEvidence.add(EvidenceDetailsResponse(evidence.evidenceHash,
                                    evidence.evidenceId,
                                    EvidenceType.LINKED,
                                    linkedEvidenceDetails,
                                    UserResponse(evidence.user.publicWalletID),
                                    evidence.removed))
            }
        }

        return ApiResponse(status = ResponseStatus.SUCCESSFUL, responseObject = GetAllEvidenceResponse(uploadedEvidence, linkedEvidence))
    }

    fun removeEvidence(userId: String, agreementId: UUID, evidenceId: String): ApiResponse<Objects> {
        //the agreement doesn't exist
        if(!agreementsRepository.existsById(agreementId))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.agreementDoesNotExist)
        val agreement = agreementsRepository.getById(agreementId)

        //the user doesn't exist
        if(!userRepository.existsById(userId))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.userDoesNotExist)
        val user = userRepository.getById(userId)

        //the user isn't party to the agreement
        val userList = userRepository.getUsersByAgreementsContaining(agreement)
        if(!userList.contains(user))
            return  ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.userNotPartOfAgreement)

        //evidence doesn't exist
        if(!evidenceRepository.existsById(UUID.fromString(evidenceId)))
            return ApiResponse(status = ResponseStatus.FAILED, message = commonResponseErrorMessages.evidenceDoesNotExist)
        val evidence = evidenceRepository.getById(UUID.fromString(evidenceId))

        //user doesn't own the file
        if(evidence.user != user)
            return ApiResponse(ResponseStatus.FAILED, message = "User does not own the file")

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

        return ApiResponse(ResponseStatus.SUCCESSFUL)
    }

    fun computeHash(file: MultipartFile): String{
        val byteHash = MessageDigest.getInstance("MD5").digest(file.bytes)
        val bigRep = BigInteger(1, byteHash)
        return String.format("%032x", bigRep)
    }
}