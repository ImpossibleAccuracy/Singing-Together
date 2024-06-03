package com.singing.app.di.module

import com.singing.app.data.datasource.declaration.*
import com.singing.app.data.datasource.impl.*
import org.koin.core.module.dsl.new
import org.koin.dsl.module

val dataSourceModule = module {
    single<AuthDataSource> { new(::AuthDataSourceImpl) }
    single<AccountDataSource.Remote> { new(::AccountRemoteDataSourceImpl) }
    single<PublicationDataSource.Remote> { new(::PublicationRemoteDataSourceImpl) }
    single<RecentTracksDataSource.Local> { new(::RecentTracksLocalDataSourceImpl) }
    single<RecordFileDataSource> { new(::RecordFileDataSourceImpl) }
    single<RecordDataSource.Local> { new(::RecordLocalDataSourceImpl) }
    single<RecordDataSource.Remote> { new(::RecordRemoteDataSourceImpl) }
}