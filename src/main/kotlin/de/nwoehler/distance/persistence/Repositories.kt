package de.nwoehler.distance.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DistanceMeasurementRepository : JpaRepository<DistanceMeasurement, Long> {
    @Query("SELECT new de.nwoehler.distance.persistence.GroupedQueryResult(avg(d.durationInTraffic/60), d.destination, d.origin) FROM DistanceMeasurement d group by d.destination, d.origin")
    fun averageTrafficInMinByDestinationAndOrigin(): List<GroupedQueryResult>
}