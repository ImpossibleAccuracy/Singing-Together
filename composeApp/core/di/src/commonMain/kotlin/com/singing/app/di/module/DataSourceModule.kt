package com.singing.app.di.module

import com.singing.app.data.datasource.declaration.AccountDataSource
import com.singing.app.data.datasource.declaration.PublicationDataSource
import com.singing.app.data.datasource.declaration.RecentTracksDataSource
import com.singing.app.data.datasource.declaration.RecordDataSource
import com.singing.app.data.datasource.declaration.RecordFileDataSource
import com.singing.app.data.datasource.impl.AccountRemoteDataSourceImpl
import com.singing.app.data.datasource.impl.PublicationRemoteDataSourceImpl
import com.singing.app.data.datasource.impl.RecentTracksLocalDataSourceImpl
import com.singing.app.data.datasource.impl.RecordFileDataSourceImpl
import com.singing.app.data.datasource.impl.RecordLocalDataSourceImpl
import com.singing.app.data.datasource.impl.RecordRemoteDataSourceImpl
import org.koin.core.module.dsl.new
import org.koin.dsl.module

val dataSourceModule = module {
    single<AccountDataSource.Remote> { new(::AccountRemoteDataSourceImpl) }
    single<PublicationDataSource.Remote> { new(::PublicationRemoteDataSourceImpl) }
    single<RecentTracksDataSource.Local> { new(::RecentTracksLocalDataSourceImpl) }
    single<RecordFileDataSource> { new(::RecordFileDataSourceImpl) }
    single<RecordDataSource.Local> { new(::RecordLocalDataSourceImpl) }
    single<RecordDataSource.Remote> { new(::RecordRemoteDataSourceImpl) }
}