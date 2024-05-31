package com.singing.feature.main.viewmodel

import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.UserData

data class PublicationDetailsUiState(
    val user: UserData? = null,
    val publication: Publication,
)