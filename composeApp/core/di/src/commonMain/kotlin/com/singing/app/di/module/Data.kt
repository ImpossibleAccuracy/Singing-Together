package com.singing.app.di.module

import com.singing.app.data.features.RecordPlayerImpl
import com.singing.app.data.provider.UserProviderImpl
import com.singing.app.data.repository.AccountRepositoryImpl
import com.singing.app.data.repository.PublicationRepositoryImpl
import com.singing.app.data.repository.RecordRepositoryImpl
import com.singing.app.data.repository.RecentTrackRepositoryImpl
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.repository.AccountRepository
import com.singing.app.domain.repository.PublicationRepository
import com.singing.app.domain.repository.RecentTrackRepository
import com.singing.app.domain.repository.RecordRepository
import org.koin.dsl.module

val dataModule = module {
    factory<RecordPlayer> { RecordPlayerImpl(get()) }
    single<UserProvider> { UserProviderImpl() }

    // TODO: make factory instead of single
    single<PublicationRepository> { PublicationRepositoryImpl(get(), get()) }
    single<RecordRepository> { RecordRepositoryImpl() }
    single<RecentTrackRepository> { RecentTrackRepositoryImpl() }
    single<AccountRepository> { AccountRepositoryImpl() }
}
