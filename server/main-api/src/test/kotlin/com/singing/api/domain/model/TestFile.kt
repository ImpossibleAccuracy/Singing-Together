package com.singing.api.domain.model

import java.io.File

data class TestFile(
    val get: () -> File,
    val mimeType: String,
    val record: (TestUser) -> RecordEntity,
)
