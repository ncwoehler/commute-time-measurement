package de.nwoehler.distance.controller

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
        val results = resultService.getHomesWithAggregatedCommuteTimes().sortedBy { it.toWork?.average }
        model.addAttribute("results", results)
        return "home"
    }
}
