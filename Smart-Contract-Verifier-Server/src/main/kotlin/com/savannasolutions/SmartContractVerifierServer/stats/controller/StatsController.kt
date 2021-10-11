package com.savannasolutions.SmartContractVerifierServer.stats.controller

import com.savannasolutions.SmartContractVerifierServer.stats.requests.DetailedRequest
import com.savannasolutions.SmartContractVerifierServer.stats.services.StatsService
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@CrossOrigin
@RestController
class StatsController constructor(private val statsService: StatsService) {
    @GetMapping("/stats")
    fun generalStats() = statsService.generalStats()

    @GetMapping("/stats/detailed")
    fun detailedStats(@RequestBody detailedRequest: DetailedRequest)
    = statsService.detailedStats(detailedRequest.startDate, detailedRequest.endDate)
}