package com.singing.api.service.resources

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app.resources")
data class ResourcesProperties(
    val resourcesServerUrl: String,
)
