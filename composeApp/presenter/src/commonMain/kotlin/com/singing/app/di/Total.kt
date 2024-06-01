package com.singing.app.di

import com.singing.app.data.setup.PlatformInitParams
import com.singing.app.data.setup.database.DatabaseParameters
import com.singing.app.data.setup.file.FileStoreProperties
import com.singing.app.data.setup.ktor.ApiParameters
import com.singing.app.di.module.dataModule
import com.singing.app.di.module.useCaseModule
import com.singing.app.domain.model.UserData
import com.singing.app.domain.provider.UserProvider
import com.singing.feature.account.profile.di.accountProfileModule
import com.singing.feature.community.di.communityModule
import com.singing.feature.main.di.mainModule
import com.singing.feature.publication.details.di.publicationDetailsModule
import com.singing.feature.record.di.recordDetailModule
import com.singing.feature.record.list.di.recordListModule
import com.singing.feature.recording.di.recordingModule
import com.singing.feature.recording.setup.recordingSetupModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.module.Module
import org.koin.dsl.module

fun totalAppModules(
    init: PlatformInitParams,
    apiParameters: ApiParameters,
    databaseParameters: DatabaseParameters,
    storeProperties: FileStoreProperties,
): List<Module> = mutableListOf<Module>().apply {
    add(
        module {
            single<UserProvider> {
                object : UserProvider {
                    override val userFlow: StateFlow<UserData?>
                        get() = MutableStateFlow(null)

                    override val token: String?
                        get() = null

                    override fun setCurrentUser(userData: UserData) {
                        TODO("Not yet implemented")
                    }

                    override fun clear() {
                    }
                }
            }
        }
    )

    add(useCaseModule)
    add(dataModule(
        init = init,
        apiParameters = apiParameters,
        databaseParameters = databaseParameters,
        storeProperties = storeProperties
    ))

    add(mainModule)
    add(recordingModule)
    add(recordingSetupModule)
    add(recordListModule)
    add(recordDetailModule)
    add(communityModule)
    add(publicationDetailsModule)
    add(accountProfileModule)
}
