package com.singing.app.common.views.model.actions

data class PublicationCardActions(
    val onAuthorClick: (() -> Unit)? = null,
    val onPlay: (() -> Unit)? = null,
    val navigatePublicationDetails: () -> Unit,
)