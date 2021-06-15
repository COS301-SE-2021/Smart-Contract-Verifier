package com.savannasolutions.SmartContractVerifierServer.controllers

import com.savannasolutions.SmartContractVerifierServer.requests.AcceptConditionRequest
import com.savannasolutions.SmartContractVerifierServer.requests.GetAgreementDetailsRequest
import com.savannasolutions.SmartContractVerifierServer.requests.GetAllConditionsRequest
import com.savannasolutions.SmartContractVerifierServer.requests.RejectConditionRequest
import com.savannasolutions.SmartContractVerifierServer.services.NegotiationService
import org.springframework.web.bind.annotation.*


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
}