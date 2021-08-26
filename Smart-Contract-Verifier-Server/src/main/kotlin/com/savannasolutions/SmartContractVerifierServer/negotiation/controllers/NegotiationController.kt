package com.savannasolutions.SmartContractVerifierServer.negotiation.controllers

import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.*
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController
class NegotiationController constructor(private val negotiationService: NegotiationService,) {

    @GetMapping("/user/{userId}/agreement/{agreementId}")
    fun getAgreementDetails(@PathVariable userId:String, @PathVariable agreementId: UUID,) =
        negotiationService.getAgreementDetails(userId, agreementId)

    @GetMapping("/user/{userId}/agreement/{agreementId}/condition")
    fun getAllConditions(@PathVariable userId: String, @PathVariable agreementId: UUID,) =
        negotiationService.getAllConditions(userId, agreementId)

    @PutMapping("/user/{userId}/agreement/{agreementId}/condition/{conditionId}/accept")
    fun acceptCondition(@PathVariable userId: String, @PathVariable agreementId: UUID, @PathVariable conditionId: UUID,) =
        negotiationService.acceptCondition(userId, agreementId, conditionId)

    @PostMapping("/user/{userId}/agreement/{agreementId}/condition/{conditionId}/reject")
    fun rejectCondition(@PathVariable userId: String, @PathVariable agreementId: UUID, @PathVariable conditionId: UUID,) =
        negotiationService.rejectCondition(userId, agreementId, conditionId)

    @GetMapping("/hello")
    fun hello() = "HELLO!"

    @PostMapping("/user/{userId}/agreement/{agreementId}/condition")
    fun createCondition(@PathVariable userId: String,
                        @PathVariable agreementId: UUID,
                        @RequestBody createConditionRequest: CreateConditionRequest,) =
        negotiationService.createCondition(userId, agreementId, createConditionRequest)

    @PostMapping("/user/{userId}/agreement")
    fun createAgreement(@PathVariable userId: String, @RequestBody createAgreementRequest: CreateAgreementRequest,) =
        negotiationService.createAgreement(userId, createAgreementRequest)

    @GetMapping("/user/{userId}/agreement/{agreementId}/condition/{conditionId}")
    fun getConditionDetails(@PathVariable userId: String, @PathVariable agreementId: UUID, @PathVariable conditionId: UUID,) =
        negotiationService.getConditionDetails(userId, agreementId, conditionId)

    @PostMapping("/user/{userId}/agreement/{agreementId}/condition/payment")
    fun setPaymentCondition(@PathVariable userId: String,
                            @PathVariable agreementId: UUID,
                            @RequestBody setDurationConditionRequest: SetPaymentConditionRequest,) =
        negotiationService.setPaymentCondition(userId, agreementId, setPaymentConditionRequest)

    @PostMapping("/user/{userId}/agreement/{agreementId}/condition/duration")
    fun setDurationCondition(@PathVariable userId: String,
                            @PathVariable agreementId: UUID,
                            @RequestBody setDurationConditionRequest: SetDurationConditionRequest,) =
        negotiationService.setDurationCondition(userId, agreementId, setDurationConditionRequest)

    @GetMapping("/judge/{userId}/agreement")
    fun getJudgingAgreements(@PathVariable userId: String,) =
        negotiationService.getJudgingAgreements(userId)
}