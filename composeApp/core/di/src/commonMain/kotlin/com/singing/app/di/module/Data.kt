package com.singing.app.di.module

import com.singing.app.data.features.RecordPlayerImpl
import com.singing.app.data.provider.UserProviderImpl
import com.singing.app.data.repository.*
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.repository.*
import org.koin.dsl.module

val dataModule = module {
    factory<RecordPlayer> { RecordPlayerImpl(get()) }
    single<UserProvider> { UserProviderImpl() }

    factory<NoteRepository> { NoteRepositoryImpl() }
    factory<PublicationRepository> { PublicationRepositoryImpl(get(), get()) }
    factory<PublicationSearchRepository> { PublicationSearchRepositoryImpl(get()) }
    factory<PublicationTagRepository> { PublicationTagRepositoryImpl() }
    factory<RecordRepository> { RecordRepositoryImpl() }
    factory<RecentTrackRepository> { RecentTrackRepositoryImpl() }
    factory<AccountRepository> { AccountRepositoryImpl() }
}
