package com.singing.feature.recording.save

import cafe.adriel.voyager.core.model.ScreenModel
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.payload.RecordSaveData
import com.singing.app.domain.repository.RecordRepository

sealed interface RecordSaveStrategy {
    data object Locally : RecordSaveStrategy

    data object Remote : RecordSaveStrategy
}

class RecordSaveViewModel(
    private val recordRepository: RecordRepository,
) : ScreenModel {
    suspend fun save(data: RecordSaveData, saveStrategy: RecordSaveStrategy): RecordData {
        return recordRepository.saveRecord(data, saveStrategy == RecordSaveStrategy.Remote)
    }
}