package com.singing.app.common.views

import com.singing.app.common.views.model.state.PublicationUiData
import com.singing.app.common.views.model.state.UserUiData
import com.singing.app.domain.model.Publication
import kotlinx.collections.immutable.toImmutableList
import nl.jacobras.humanreadable.HumanReadable

fun Publication.toPublicationCardData(showActions: Boolean) = PublicationUiData(
    author = UserUiData(
        avatar = this.author.avatar,
        username = this.author.username,
    ),
    createdAt = HumanReadable.timeAgo(this.createdAt.instant),
    description = this.description,
    tags = this.tags.map { it.title }.toImmutableList(),
    showActions = showActions,
)
