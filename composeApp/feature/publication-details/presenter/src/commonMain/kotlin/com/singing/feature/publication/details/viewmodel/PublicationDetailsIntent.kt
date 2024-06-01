package com.singing.feature.publication.details.viewmodel

import com.singing.app.domain.model.Publication

sealed interface PublicationDetailsIntent {
    data class DeletePublication(val publication: Publication) : PublicationDetailsIntent
}