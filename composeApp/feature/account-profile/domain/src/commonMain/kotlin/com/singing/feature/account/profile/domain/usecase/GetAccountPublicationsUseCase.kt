package com.singing.feature.account.profile.domain.usecase

import com.singing.app.domain.model.UserData
import com.singing.app.domain.repository.PublicationRepository

class GetAccountPublicationsUseCase(
    private val publicationRepository: PublicationRepository,
) {
    operator fun invoke(account: UserData) =
        publicationRepository.getAccountPublications(account.id)
}