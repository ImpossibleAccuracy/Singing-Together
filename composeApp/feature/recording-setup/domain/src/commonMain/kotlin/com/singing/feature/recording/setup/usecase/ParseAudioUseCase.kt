package com.singing.feature.recording.setup.usecase

import com.singing.app.audio.createTrackAudioParser
import com.singing.app.audio.processAudioFile
import com.singing.app.base.ComposeFile
import com.singing.app.domain.model.TrackParseResult
import com.singing.config.track.TrackProperties
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.yield

class ParseAudioUseCase {
    suspend operator fun invoke(file: ComposeFile): TrackParseResult {
        val audioFile = processAudioFile(file)
            ?: throw IllegalArgumentException("Cannot process file: $file")

        val parser = createTrackAudioParser(audioFile.file, TrackProperties.defaultFilters)

        val data = parser
            .parse()
            .map {
                it.positionMs to it.frequency
            }
            .toList()
            .associate { it }

        return TrackParseResult(
            selectedAudio = audioFile,
            data = data.toPersistentMap(),
        )
    }
}