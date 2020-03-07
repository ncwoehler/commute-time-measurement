package de.nwoehler.distance.service

import de.nwoehler.distance.config.CommuteJobConfig
import de.nwoehler.distance.persistence.DistanceMeasurementRepository
import de.nwoehler.distance.persistence.GroupedQueryResult
import org.springframework.stereotype.Service

data class Commute(
    val homeAddress: String,
    var toWork: CommuteTimes? = null,
    var fromWork: CommuteTimes? = null
)

data class CommuteTimes(
    val average: Long,
    val min: Long,
    val max: Long,
    val count: Long,
    val distance: Long
)

@Service
class ResultService(
    private val distanceMeasurementRepository: DistanceMeasurementRepository,
    private val commuteJobConfig: CommuteJobConfig
) {
    fun getHomesWithAggregatedCommuteTimes(): List<Commute> {

        // create map with all possible home addressed to respective commute times
        val resultMap = mutableMapOf(*commuteJobConfig.home.map { Pair(it, Commute(it)) }.toTypedArray())

        // query stored results and assign each to the specific direction and address
        val groupQueryResults = distanceMeasurementRepository.averageTrafficInMinByDestinationAndOrigin()
        groupQueryResults.forEach {
            if (it.destination in resultMap.keys) {
                resultMap[it.destination]?.fromWork = toCommuteTimes(it)
            } else {
                resultMap[it.origin]?.toWork = toCommuteTimes(it)
            }
        }
        return resultMap.values.toList()
    }

    private fun toCommuteTimes(queryResult: GroupedQueryResult) = CommuteTimes(
        average = queryResult.avgTraffic.toLong(),
        min = queryResult.minTraffic.toLong(),
        max = queryResult.maxTraffic.toLong(),
        distance = queryResult.avgDistance.toLong(),
        count = queryResult.count
    )

}