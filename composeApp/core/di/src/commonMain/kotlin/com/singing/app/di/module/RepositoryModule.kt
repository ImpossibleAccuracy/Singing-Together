package com.singing.app.di.module

import com.singing.app.data.features.RecordPlayerImpl
import com.singing.app.data.repository.AccountRepositoryImpl
import com.singing.app.data.repository.NoteRepositoryImpl
import com.singing.app.data.repository.PublicationRepositoryImpl
import com.singing.app.data.repository.PublicationSearchRepositoryImpl
import com.singing.app.data.repository.PublicationTagRepositoryImpl
import com.singing.app.data.repository.RecentTrackRepositoryImpl
import com.singing.app.data.repository.RecordRepositoryImpl
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.repository.AccountRepository
import com.singing.app.domain.repository.NoteRepository
import com.singing.app.domain.repository.PublicationRepository
import com.singing.app.domain.repository.PublicationSearchRepository
import com.singing.app.domain.repository.PublicationTagRepository
import com.singing.app.domain.repository.RecentTrackRepository
import com.singing.app.domain.repository.RecordRepository
import org.koin.core.module.dsl.new
import org.koin.dsl.module

val repositoryModule = module {
    factory<RecordPlayer> { new(::RecordPlayerImpl) }

    single<AccountRepository> { new(::AccountRepositoryImpl) }
    single<NoteRepository> { new(::NoteRepositoryImpl) }
    single<PublicationRepository> { new(::PublicationRepositoryImpl) }
    single<PublicationSearchRepository> { new(::PublicationSearchRepositoryImpl) }
    single<PublicationTagRepository> { new(::PublicationTagRepositoryImpl) }
    single<RecentTrackRepository> { new(::RecentTrackRepositoryImpl) }
    single<RecordRepository> { new(::RecordRepositoryImpl) }
}
