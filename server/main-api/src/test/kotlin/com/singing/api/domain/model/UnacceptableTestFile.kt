package com.singing.api.domain.model

import java.io.File

data class UnacceptableTestFile(
    val get: () -> File,
    val mimeType: String?,
)
