package de.nwoehler.distance.persistence

import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class DistanceMeasurement(
    val origin: String,
    val destination: String,
    val distanceInMeter: Long,
    val duration: Long,
    val durationInTraffic: Long,

    @Id
    @GeneratedValue
    val id: Long? = null,

    @CreationTimestamp
    val createdAt: Instant? = null
)