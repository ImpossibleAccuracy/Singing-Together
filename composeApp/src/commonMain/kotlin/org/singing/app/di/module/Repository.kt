package org.singing.app.di.module

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.singing.app.domain.repository.publication.PublicationRepository
import org.singing.app.domain.repository.record.RecordPlayer
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.repository.track.RecentTrackRepository

val repositoryModule = module {
    singleOf(::RecordPlayer)

    singleOf(::PublicationRepository)
    singleOf(::RecordRepository)
    singleOf(::RecentTrackRepository)
}
