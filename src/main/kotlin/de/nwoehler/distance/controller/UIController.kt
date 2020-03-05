package de.nwoehler.distance.controller

import de.nwoehler.distance.config.CommuteJobConfig
import de.nwoehler.distance.persistence.DistanceMeasurementRepository
import de.nwoehler.distance.service.ResultService
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

private val logger = KotlinLogging.logger { }

@Controller
class UIController(
    private val resultService: ResultService
) {

    @GetMapping("/ui/home")
    fun main(model: Model): String? {
        val results = resultService.getDestinationsWithAverageTime().sortedBy { it.morning }
        model.addAttribute("results", results)
        return "home"
    }
}
