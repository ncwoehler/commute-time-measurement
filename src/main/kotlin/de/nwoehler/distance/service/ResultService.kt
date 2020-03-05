package de.nwoehler.distance.service

import de.nwoehler.distance.config.CommuteJobConfig
import de.nwoehler.distance.persistence.DistanceMeasurementRepository
import org.springframework.stereotype.Service

data class CommuteResult(
    val destination: String,
    var morning: Long?  = null,
    var afternoon: Long?  = null
)

@Service
class ResultService(
    private val distanceMeasurementRepository: DistanceMeasurementRepository,
    private val commuteJobConfig: CommuteJobConfig
) {
    fun getDestinationsWithAverageTime(): MutableCollection<CommuteResult> {
        val groupQueryResults = distanceMeasurementRepository.averageTrafficInMinByDestinationAndOrigin()

        val resultMap = mutableMapOf<String, CommuteResult>()
        commuteJobConfig.home.forEach { resultMap[it] = CommuteResult(it) }
        val homeSet = commuteJobConfig.home.toSet()

        groupQueryResults.forEach {
            if (it.destination in homeSet) {
                resultMap[it.destination]?.afternoon = it.traffic.toLong()
            } else {
                resultMap[it.origin]?.morning = it.traffic.toLong()
            }
        }
        return resultMap.values
    }

}