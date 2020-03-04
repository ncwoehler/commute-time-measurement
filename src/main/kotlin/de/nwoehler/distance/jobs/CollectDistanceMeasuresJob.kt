package de.nwoehler.distance.jobs

import com.google.maps.DistanceMatrixApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DistanceMatrix
import com.google.maps.model.TravelMode
import com.google.maps.model.Unit
import de.nwoehler.distance.config.CommuteJobConfig
import de.nwoehler.distance.persistence.DistanceMeasurement
import de.nwoehler.distance.persistence.DistanceMeasurementRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@Service
class CollectDistanceMeasuresJob(
    private val distanceMeasurementRepository: DistanceMeasurementRepository,
    private val commuteJobConfig: CommuteJobConfig
) {

    @Value("\${google.api.key}")
    lateinit var googleApiKey: String

    lateinit var apiContext: GeoApiContext

    @PostConstruct
    fun createGeoContext() {
        apiContext = GeoApiContext.Builder().apiKey(googleApiKey).disableRetries().build()

        logger.info { "Measurement Job started." }
        logger.info { "Home locations: ${commuteJobConfig.home.joinToString()}" }
        logger.info { "Work locations: ${commuteJobConfig.work.joinToString()}" }
    }

    @Scheduled(cron = "\${jobs.collect.morning.cron}")
    fun collectMorningCommuteTimes() {
        logger.info { "Measuring morning commute times..." }
        measureCommuteTimes(commuteJobConfig.home, commuteJobConfig.work)
    }

    @Scheduled(cron = "\${jobs.collect.afternoon.cron}")
    fun collectAfternoonCommuteTimes() {
        logger.info { "Measuring afternoon commute times..." }
        measureCommuteTimes(commuteJobConfig.work, commuteJobConfig.home)
    }

    private fun measureCommuteTimes(origins: Array<String>, destinations: Array<String>) {
        logger.info { "Destination location: ${destinations.joinToString(" | ")}" }
        logger.info { "Origin location: ${origins.joinToString(" | ")}" }

        DistanceMatrixApi.getDistanceMatrix(apiContext, origins, destinations)
            .departureTime(Instant.now())
            .mode(TravelMode.DRIVING)
            .units(Unit.METRIC)
            .language("de")
            .awaitIgnoreError()?.let { matrix ->
                transformAndStoreResponse(origins, destinations, matrix)
            } ?: run {
            logger.error { "Failed to retrieve distance matrix informations.." }
        }
    }

    private fun transformAndStoreResponse(
        origins: Array<String>,
        destinations: Array<String>,
        distanceMatrix: DistanceMatrix
    ) {
        distanceMatrix.rows.forEachIndexed { rowIndex, row ->
            val origin = origins[rowIndex]
            row.elements.forEachIndexed { elementIndex, element ->
                val destination = destinations[elementIndex]
                logger.info { "" }
                logger.info { "$origin -> $destination:" }
                logger.info { " Distance: ${element.distance}" }
                logger.info { " Duration in traffic: ${element.durationInTraffic}" }
                logger.info { " Status: ${element.status}" }

                val measurement = DistanceMeasurement(
                    origin,
                    destination,
                    element.distance.inMeters,
                    element.duration.inSeconds,
                    element.durationInTraffic.inSeconds
                )
                distanceMeasurementRepository.save(measurement)
            }
        }
    }
}