package com.singing.app.data.datamapper.impl

import com.singing.app.base.openFile
import com.singing.app.data.sqldelight.record.RecentTrackEntity
import com.singing.app.data.sqldelight.record.RecordEntity
import com.singing.app.data.sqldelight.record.RecordItemEntity
import com.singing.app.domain.model.ExtendedKey
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.PublicationTag
import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData
import com.singing.app.domain.model.UserInfo
import com.singing.app.domain.model.stable.StableInstant
import com.singing.domain.model.AudioFile
import com.singing.domain.model.RecordPoint
import com.singing.domain.payload.dto.AccountDto
import com.singing.domain.payload.dto.AccountInfoDto
import com.singing.domain.payload.dto.CategoryInfoDto
import com.singing.domain.payload.dto.PublicationDto
import com.singing.domain.payload.dto.RecordDto
import com.singing.domain.payload.dto.RecordPointDto
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant

fun map(source: AccountDto): UserData =
    UserData(
        id = source.id!!,
        username = source.username!!,
        avatar = source.avatar,
    )

fun map(source: AccountInfoDto): UserInfo =
    UserInfo(
        publicationsCount = source.publicationsCount!!,
        registeredAt = StableInstant(source.registeredAt!!.toKotlinInstant()),
    )

fun map(source: CategoryInfoDto): PublicationTagStatistics =
    PublicationTagStatistics(
        title = source.title!!,
        publications = source.publications!!,
    )

fun map(source: PublicationDto, localRecordId: Int?): Publication =
    Publication(
        id = source.id!!,
        author = map(source.author!!),
        createdAt = StableInstant(source.createdAt!!.toKotlinInstant()),
        description = source.description!!,
        record = map(source.record!!, localRecordId),
        tags = source.tags!!
            .map { map(it) }
            .toPersistentList(),
    )

fun map(source: RecordDto, localId: Int?): RecordData =
    when (source.accuracy) {
        null -> RecordData.Vocal(
            key = ExtendedKey(
                localId = localId,
                remoteId = source.id!!,
            ),
            createdAt = StableInstant(source.createdAt!!.toKotlinInstant()),
            name = source.title,
            duration = source.duration!!,
            isPublished = source.isPublished!!,
            creatorId = source.creatorId,
        )

        else -> RecordData.Cover(
            key = ExtendedKey(
                localId = localId,
                remoteId = source.id!!,
            ),
            createdAt = StableInstant(source.createdAt!!.toKotlinInstant()),
            name = source.title,
            accuracy = source.accuracy!!.toInt(),
            duration = source.duration!!,
            isPublished = source.isPublished!!,
            creatorId = source.creatorId,
        )
    }

fun map(source: String): PublicationTag =
    PublicationTag(source)

fun map(source: RecentTrackEntity): RecentTrack =
    RecentTrack(
        id = source.id.toInt(),
        createdAt = StableInstant(Instant.parse(source.createdAt)),
        audioFile = AudioFile(
            file = openFile(source.path),
            duration = source.duration,
            name = source.name,
        ),
        isFavourite = source.isFavourute > 0,
    )

fun map(source: RecordEntity): RecordData =
    when (source.accuracy) {
        null -> RecordData.Vocal(
            key = ExtendedKey(
                localId = source.id.toInt(),
                remoteId = source.remoteId?.toInt(),
            ),
            createdAt = StableInstant(Instant.parse(source.createdAt)),
            name = source.title,
            duration = source.duration,
            isPublished = source.isPublished > 0,
            creatorId = source.creatorId?.toInt(),
        )

        else -> RecordData.Cover(
            key = ExtendedKey(
                localId = source.id.toInt(),
                remoteId = source.remoteId?.toInt(),
            ),
            createdAt = StableInstant(Instant.parse(source.createdAt)),
            name = source.title,
            accuracy = source.accuracy.toInt(),
            duration = source.duration,
            isPublished = source.isPublished > 0,
            creatorId = source.creatorId?.toInt(),
        )
    }

fun map(source: RecordPointDto): RecordPoint =
    RecordPoint(
        time = source.time!!,
        first = source.first,
        second = source.second,
    )

fun map(source: RecordItemEntity): RecordPoint =
    RecordPoint(
        time = source.time,
        first = source.frequency,
        second = source.trackFrequency,
    )
