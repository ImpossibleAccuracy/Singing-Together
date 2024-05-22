package com.singing.api.service.record.data

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.time.Duration

@Validated
@ConfigurationProperties(prefix = "app.record")
data class RecordProcessingProperties(
    val maxParsingDuration: Duration
)
