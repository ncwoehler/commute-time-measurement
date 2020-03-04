package de.nwoehler.distance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class DistanceApplication

fun main(args: Array<String>) {
	runApplication<DistanceApplication>(*args)
}
