package de.nwoehler.distance.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("jobs.collect")
class CommuteJobConfig {
    lateinit var home: Array<String>
    lateinit var work: Array<String>
}