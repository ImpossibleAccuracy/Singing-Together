package com.singing.api.domain

import com.singing.api.domain.model.AccountEntity
import com.singing.api.domain.model.CategoryInfoEntity
import com.singing.api.domain.model.PublicationEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.api.domain.model.RecordItemEntity
import com.singing.domain.payload.dto.AccountDto
import com.singing.domain.payload.dto.AccountInfoDto
import com.singing.domain.payload.dto.CategoryInfoDto
import com.singing.domain.payload.dto.PublicationDto
import com.singing.domain.payload.dto.RecordDto
import com.singing.domain.payload.dto.RecordPointDto
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


private var isoFormatter = DateTimeFormatter
    .ofPattern("yyyy-MM-dd'T'hh:mm:ssxxx")
    .withZone(ZoneId.systemDefault())

fun makeAccountInfoDto(publicationsCount: Long, registeredAt: Instant) =
    AccountInfoDto(
        publicationsCount = publicationsCount,
        registeredAt = isoFormatter.format(registeredAt),
    )

fun RecordEntity.toDto() = RecordDto(
    id = this.id,
    createdAt = isoFormatter.format(this.createdAt),
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
    createdAt = isoFormatter.format(this.createdAt),
    description = this.description,
    author = this.account?.toDto(),
    record = this.record?.toDto(),
    tags = this.tags.map { it.title!! },
)
