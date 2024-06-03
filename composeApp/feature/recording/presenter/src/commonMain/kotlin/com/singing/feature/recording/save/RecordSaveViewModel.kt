package com.singing.feature.recording.save

import cafe.adriel.voyager.core.model.ScreenModel
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.payload.RecordSaveData
import com.singing.app.domain.repository.RecordRepository
import com.singing.app.domain.usecase.FindNoteUseCase

sealed interface RecordSaveStrategy {
    data object Locally : RecordSaveStrategy

    data object Remote : RecordSaveStrategy
}

class RecordSaveViewModel(
    private val findNoteUseCase: FindNoteUseCase,
    private val recordRepository: RecordRepository,
) : ScreenModel {
    fun getNote(frequency: Double) = findNoteUseCase(frequency)

    suspend fun save(data: RecordSaveData, saveStrategy: RecordSaveStrategy): DataState<RecordData> {
        return recordRepository.saveRecord(data, saveStrategy == RecordSaveStrategy.Remote)
    }
}