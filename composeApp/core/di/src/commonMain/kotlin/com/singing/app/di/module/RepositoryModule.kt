package com.singing.app.di.module

import com.singing.app.data.features.RecordPlayerImpl
import com.singing.app.data.repository.*
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.repository.*
import org.koin.core.module.dsl.new
import org.koin.dsl.module

val repositoryModule = module {
    factory<RecordPlayer> { new(::RecordPlayerImpl) }

    single<AccountRepository> { new(::AccountRepositoryImpl) }
    single<AuthRepository> { new(::AuthRepositoryImpl) }
    single<NoteRepository> { new(::NoteRepositoryImpl) }
    single<PublicationRepository> { new(::PublicationRepositoryImpl) }
    single<PublicationSearchRepository> { new(::PublicationSearchRepositoryImpl) }
    single<PublicationTagRepository> { new(::PublicationTagRepositoryImpl) }
    single<RecentTrackRepository> { new(::RecentTrackRepositoryImpl) }
    single<RecordRepository> { new(::RecordRepositoryImpl) }
}
