package com.singing.app.common.views

import com.singing.app.common.views.model.state.TrackUiData
import com.singing.app.domain.model.RecentTrack
import nl.jacobras.humanreadable.HumanReadable
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun RecentTrack.toTrackCardData() = TrackUiData(
    filename = this.audioFile.name,
    duration = HumanReadable.duration(
        this.audioFile.duration.milliseconds
            .inWholeSeconds.seconds
    ),
    isFavourite = this.isFavourite
)