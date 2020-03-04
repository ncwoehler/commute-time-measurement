package de.nwoehler.distance.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DistanceMeasurementRepository : JpaRepository<DistanceMeasurement, Long>