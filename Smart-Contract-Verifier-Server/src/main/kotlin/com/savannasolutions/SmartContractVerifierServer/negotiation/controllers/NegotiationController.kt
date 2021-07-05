package com.savannasolutions.SmartContractVerifierServer.negotiation.controllers

import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.*
import com.savannasolutions.SmartContractVerifierServer.negotiation.services.NegotiationService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/negotiation")
class NegotiationController constructor(private val negotiationService: NegotiationService) {

    @PostMapping("/get-agreement-details")
    fun getAgreementDetails(@RequestBody getAgreementDetailsRequest: GetAgreementDetailsRequest) =
        negotiationService.getAgreementDetails(getAgreementDetailsRequest)

    @PostMapping("/get-all-conditions")
    fun getAllConditions(@RequestBody getAllConditionsRequest: GetAllConditionsRequest) =
        negotiationService.getAllConditions(getAllConditionsRequest)

    @PostMapping("/accept-condition")
    fun acceptCondition(@RequestBody acceptConditionRequest: AcceptConditionRequest) =
        negotiationService.acceptCondition(acceptConditionRequest)

    @PostMapping("/reject-condition")
    fun rejectCondition(@RequestBody rejectConditionRequest: RejectConditionRequest) =
        negotiationService.rejectCondition(rejectConditionRequest)

    @GetMapping("/hello")
    fun hello() = "HELLO!"

    @PostMapping("/create-condition")
    fun createCondition(@RequestBody createConditionRequest: CreateConditionRequest) =
            negotiationService.createCondition(createConditionRequest)

    @PostMapping("/create-agreement")
    fun createAgreement(@RequestBody createAgreementRequest: CreateAgreementRequest) =
            negotiationService.createAgreement(createAgreementRequest)

    @PostMapping("/seal-agreement")
    fun sealAgreement(@RequestBody sealAgreementRequest: SealAgreementRequest) =
            negotiationService.sealAgreement(sealAgreementRequest)

    @PostMapping("/get-condition-details")
    fun getConditionDetails(@RequestBody getConditionDetailsRequest: GetConditionDetailsRequest) =
            negotiationService.getConditionDetails(getConditionDetailsRequest)

    @PostMapping("/set-payment-condition")
    fun setPaymentCondition(@RequestBody setPaymentConditionRequest: SetPaymentConditionRequest) =
            negotiationService.setPaymentCondition(setPaymentConditionRequest)

    @PostMapping("/set-duration-condition")
    fun setPaymentCondition(@RequestBody setDurationConditionRequest: SetDurationConditionRequest) =
            negotiationService.setDurationCondition(setDurationConditionRequest)
}