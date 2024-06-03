package com.singing.app.data.datasource.declaration

import com.singing.app.base.ComposeFile
import com.singing.domain.model.RecordPoint

interface RecordInfoDataSource {
    suspend fun computeRecordPoints(voiceFile: ComposeFile, trackFile: ComposeFile?): List<RecordPoint>

    suspend fun computeRecordAccuracy(points: List<RecordPoint>): Double
}