package com.singing.feature.recording.domain

import com.singing.app.domain.model.TrackParseResult
import com.singing.feature.recording.domain.model.AudioInputData
import kotlinx.coroutines.flow.Flow

interface InputListener {
    val audioInputData: Flow<AudioInputData>

    suspend fun init(
        strategy: MergeStrategy,
        lineParams: LineParams,
    )

    data class LineParams(
        val audioTrackFlow: Flow<TrackParseResult?>,
        val audioPositionFlow: Flow<Long>,
    )

    enum class MergeStrategy {
        Zip,
        Combine,
    }
}
