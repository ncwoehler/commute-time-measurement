package de.nwoehler.distance.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


private const val GROUPED_QUERY =
    """SELECT new de.nwoehler.distance.persistence.GroupedQueryResult(avg(d.durationInTraffic/60), max(d.durationInTraffic/60), min(d.durationInTraffic/60), avg(d.distanceInMeter/1000), count(d.durationInTraffic), d.origin, d.destination)
        FROM DistanceMeasurement d
        WHERE 
            (extract(HOUR FROM d.createdAt) >= 7 AND extract(HOUR FROM d.createdAt) < 9)
            OR (extract(HOUR FROM d.createdAt) >= 16 AND extract(HOUR FROM d.createdAt) < 18)
        GROUP BY d.destination, d.origin"""

@Repository
interface DistanceMeasurementRepository : JpaRepository<DistanceMeasurement, Long> {

    @Query(GROUPED_QUERY)
    fun averageTrafficInMinByDestinationAndOrigin(): List<GroupedQueryResult>

}