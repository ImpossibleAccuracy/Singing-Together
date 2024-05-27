package com.singing.app.common.views.model.state

import kotlinx.collections.immutable.ImmutableList

data class PublicationUiData(
    val author: UserUiData,
    val createdAt: String,
    val description: String,
    val tags: ImmutableList<String>,
    val showActions: Boolean = true,
)