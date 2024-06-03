package com.singing.app.data.datasource.impl

import com.singing.app.audio.compute.computeAccuracyByPoints
import com.singing.app.audio.compute.computeRecordPointsByFiles
import com.singing.app.base.ComposeFile
import com.singing.app.data.datasource.declaration.RecordInfoDataSource
import com.singing.domain.model.RecordPoint

class RecordInfoDataSourceImpl : RecordInfoDataSource {
    override suspend fun computeRecordPoints(voiceFile: ComposeFile, trackFile: ComposeFile?): List<RecordPoint> =
        computeRecordPointsByFiles(
            voiceFile,
            trackFile
        )

    override suspend fun computeRecordAccuracy(points: List<RecordPoint>): Double =
        computeAccuracyByPoints(points)
}