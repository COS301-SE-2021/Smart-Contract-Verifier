package com.savannasolutions.SmartContractVerifierServer.stats.controller

import com.savannasolutions.SmartContractVerifierServer.stats.services.StatsService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@CrossOrigin
@RestController
class StatsController constructor(private val statsService: StatsService) {
    @GetMapping("/stats")
    fun generalStats() = statsService.generalStats()

    @GetMapping("/stats")
    fun detailedStats(@RequestParam("StartDate") startDate : Date, @RequestParam("EndDate") endDate: Date)
    = statsService.detailedStats(startDate, endDate)
}