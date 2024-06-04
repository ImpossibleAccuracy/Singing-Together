package com.singing.app.domain.usecase

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.orThrow
import com.singing.app.domain.repository.PublicationRepository

class PublishRecordUseCase(
    private val uploadRecordUseCase: UploadRecordUseCase,
    private val publicationRepository: PublicationRepository,
) {
    suspend operator fun invoke(
        record: RecordData,
        description: String,
        tags: List<String>
    ): Publication {
        val actualRecord = when (record.isSavedRemote) {
            true -> record
            false -> uploadRecordUseCase(record).orThrow()
        }

        // FIXME: add DataState to ViewModel
        return (publicationRepository.publishRecord(
            actualRecord,
            description,
            tags,
        ) as DataState.Success).data
    }
}
