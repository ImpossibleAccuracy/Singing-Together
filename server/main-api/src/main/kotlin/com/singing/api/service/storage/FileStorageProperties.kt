package com.singing.api.service.storage

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app.storage")
data class FileStorageProperties(
    val tempStorePath: String,
    val regularStorePath: String,
    val fileNamePattern: String,
)
