package com.savannasolutions.SmartContractVerifierServer.evidence.controllers

import com.savannasolutions.SmartContractVerifierServer.evidence.requests.*
import com.savannasolutions.SmartContractVerifierServer.evidence.services.EvidenceService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.lang.RuntimeException
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
class EvidenceController constructor(private val evidenceService: EvidenceService,) {

    @PostMapping("/user/{userId}/agreement/{agreementId}/evidence/upload",
        consumes = [org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE])
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

    @GetMapping("/user/{userId}/agreement/{agreementId}/evidence/{evidenceHash}/download",
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    @ResponseBody
    fun  downloadEvidence(response: HttpServletResponse,
                         @PathVariable userId: String,
                         @PathVariable agreementId: UUID,
                         @PathVariable evidenceHash: String,){
        val fileResp = evidenceService.downloadEvidence(userId, agreementId, evidenceHash)
        if(fileResp.file != null) {
            response.contentType = fileResp.mimeType
            response.setHeader("Content-Disposition", "attachment; filename=" + fileResp.file?.name)
            response.setHeader("Content-Transfer-Encoding", "binary")
            try {
                val outputStream = BufferedOutputStream(response.outputStream)
                val inputStream = FileInputStream(fileResp.file)
                var len: Int
                val buffer: ByteArray = ByteArray(1024)
                var reading = true
                while (reading){
                    len=inputStream.read(buffer)
                    if(len > 0) {
                        outputStream.write(buffer, 0, len)
                    }else{
                        reading = false
                    }
                }
                outputStream.close()
                response.flushBuffer()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
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