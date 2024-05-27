package com.singing.app.domain.usecase

import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.provider.currentUser
import com.singing.app.domain.repository.PublicationRepository

class PublishRecordUseCase(
    private val userProvider: UserProvider,
    private val uploadRecordUseCase: UploadRecordUseCase,
    private val publicationRepository: PublicationRepository,
) {
    suspend operator fun invoke(record: RecordData, description: String): Publication {
        val currentUser = userProvider.currentUser
            ?: throw NullPointerException("User unauthorized")

        val actualRecord = when (record.isSavedRemote) {
            true -> record
            false -> uploadRecordUseCase(record)
        }

        return publicationRepository.publishRecord(
            currentUser,
            actualRecord,
            description
        )
    }
}
