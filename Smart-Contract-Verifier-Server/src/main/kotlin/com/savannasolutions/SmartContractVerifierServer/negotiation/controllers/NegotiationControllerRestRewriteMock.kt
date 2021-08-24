package com.savannasolutions.SmartContractVerifierServer.negotiation.controllers

import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.*
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class NegotiationControllerRestRewriteMock constructor(private val negotiationService: NegotiationService) {

    @GetMapping("/user/{userId}/agreement/{agreementId}")
    fun getAgreementDetails(@RequestBody getAgreementDetailsRequest: GetAgreementDetailsRequest) =
        negotiationService.getAgreementDetails(getAgreementDetailsRequest)

    @GetMapping("/user/{userId}/agreement/{agreementId}/condition")
    fun getAllConditions(@RequestBody getAllConditionsRequest: GetAllConditionsRequest) =
        negotiationService.getAllConditions(getAllConditionsRequest)

    @PutMapping("/user/{userId}/agreement/{agreementId}/condition/{conditionId}/accept")
    fun acceptCondition(@RequestBody acceptConditionRequest: AcceptConditionRequest) =
        negotiationService.acceptCondition(acceptConditionRequest)

    @PostMapping("/user/{userId}/agreement/{agreementId}/condition/{conditionId}/reject")
    fun rejectCondition(@RequestBody rejectConditionRequest: RejectConditionRequest) =
        negotiationService.rejectCondition(rejectConditionRequest)

    @GetMapping("/hello")
    fun hello() = "HELLO!"

    @PostMapping("/user/{userId}/agreement/{agreementId}/condition")
    fun createCondition(@RequestBody createConditionRequest: CreateConditionRequest) =
            negotiationService.createCondition(createConditionRequest)

    @PostMapping("/user/{userId}/agreement")
    fun createAgreement(@RequestBody createAgreementRequest: CreateAgreementRequest) =
            negotiationService.createAgreement(createAgreementRequest)

    @PutMapping("/user/{userId}/agreement")
    fun sealAgreement(@RequestBody sealAgreementRequest: SealAgreementRequest) =
            negotiationService.sealAgreement(sealAgreementRequest)

    @GetMapping("/user/{userId}/agreement/{agreementId}/condition/{conditionId}")
    fun getConditionDetails(@RequestBody getConditionDetailsRequest: GetConditionDetailsRequest) =
            negotiationService.getConditionDetails(getConditionDetailsRequest)

    @PostMapping("/user/{userId}/agreement/{agreementId}/condition/payment")
    fun setPaymentCondition(@RequestBody setPaymentConditionRequest: SetPaymentConditionRequest) =
            negotiationService.setPaymentCondition(setPaymentConditionRequest)

    @PostMapping("/user/{userId}/agreement/{agreementId}/condition/duration")
    fun setPaymentCondition(@RequestBody setDurationConditionRequest: SetDurationConditionRequest) =
            negotiationService.setDurationCondition(setDurationConditionRequest)

    @GetMapping("/judge/{userId}/agreement")
    fun getJudgingAgreements(@RequestBody getJudgingAgreementsRequest: GetJudgingAgreementsRequest) =
        negotiationService.getJudgingAgreements(getJudgingAgreementsRequest)
}