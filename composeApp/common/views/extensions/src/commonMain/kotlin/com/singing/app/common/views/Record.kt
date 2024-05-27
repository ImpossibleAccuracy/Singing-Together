package com.singing.app.common.views

import com.singing.app.common.views.model.state.RecordUiData
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.isTwoLineRecord
import nl.jacobras.humanreadable.HumanReadable
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun RecordData.toRecordCardData() = RecordUiData(
    accuracy = if (this.isTwoLineRecord()) this.accuracy else null,
    filename = if (this.isTwoLineRecord()) this.name else null,
    createdAt = HumanReadable.timeAgo(this.createdAt.instant),
    duration = HumanReadable.duration(
        this.duration.milliseconds
            .inWholeSeconds.seconds
    ),
    isSavedRemote = this.isSavedRemote,
    isPublished = this.isPublished,
)
