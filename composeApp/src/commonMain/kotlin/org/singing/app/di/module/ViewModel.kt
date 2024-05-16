package org.singing.app.di.module

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.koin.compose.getKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import org.singing.app.ui.screens.main.MainViewModel
import org.singing.app.ui.screens.record.audio.SelectAudioViewModel
import org.singing.app.ui.screens.record.create.viewmodel.RecordingViewModel
import org.singing.app.ui.screens.record.list.RecordListViewModel

val viewModelsModule = module {
    factoryOf(::MainViewModel)
    factoryOf(::RecordListViewModel)
    factoryOf(::SelectAudioViewModel)
    factoryOf(::RecordingViewModel)
}

@Composable
inline fun <reified T : ScreenModel> Screen.viewModels(
    saveWithNavigator: Boolean = false,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val koin = getKoin()
    val navigator = LocalNavigator.current

    return when {
        navigator != null && saveWithNavigator ->
            navigator.rememberNavigatorScreenModel {
                koin.get<T>(qualifier, parameters)
            }

        else -> rememberScreenModel(tag = qualifier?.value) {
            koin.get<T>(qualifier, parameters)
        }
    }

//    return rememberScreenModel(tag = qualifier?.value) {
//        koin.get<T>(qualifier, parameters)
//    }
}
