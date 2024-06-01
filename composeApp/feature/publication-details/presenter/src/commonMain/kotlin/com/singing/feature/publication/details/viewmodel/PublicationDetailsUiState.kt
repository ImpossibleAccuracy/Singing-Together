package com.singing.feature.publication.details.viewmodel

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.UserData

data class PublicationDetailsUiState(
    val user: UserData? = null,
    val publication: DataState<Publication>,
)