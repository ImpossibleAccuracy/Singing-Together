package com.singing.api.domain

import com.singing.api.domain.model.*
import com.singing.app.domain.dto.*

fun RecordEntity.toDto() = RecordDto(
    id = this.id,
    createdAt = this.createdAt,
    title = this.title,
    duration = this.duration,
    creatorId = this.author?.id,
    isPublished = this.publications.isNotEmpty(),
    accuracy = this.accuracy,
)

fun RecordItemEntity.toDto() = RecordPointDto(
    time = this.time,
    first = this.frequency,
    second = this.trackFrequency,
)

fun CategoryInfoEntity.toDto() = CategoryInfoDto(
    title = this.title,
    publications = this.publications,
)

fun AccountEntity.toDto() = AccountDto(
    id = this.id,
    username = this.username,
    avatar = this.avatar?.toString(), // TODO
)

fun PublicationEntity.toDto() = PublicationDto(
    id = this.id,
    createdAt = this.createdAt,
    description = this.description,
    author = this.account?.toDto(),
    record = this.record?.toDto(),
    tags = this.tags.map { it.title!! },
)
