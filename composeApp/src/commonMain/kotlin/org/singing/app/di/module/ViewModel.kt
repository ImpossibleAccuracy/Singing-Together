package org.singing.app.di.module

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.koin.compose.getKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import org.singing.app.ui.screens.main.MainViewModel
import org.singing.app.ui.screens.record.viewmodel.RecordViewModel

val viewModelsModule = module {
    single {
        MainViewModel()
    }

    factory {
        RecordViewModel()
    }
}

@Composable
inline fun <reified T : ScreenModel> Screen.viewModels(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val koin = getKoin()

    return when (val navigator = LocalNavigator.current) {
        null -> rememberScreenModel(tag = qualifier?.value) {
            koin.get<T>(qualifier, parameters)
        }

        else -> navigator.rememberNavigatorScreenModel {
            koin.get<T>(qualifier, parameters)
        }
    }

    /*return rememberScreenModel(tag = qualifier?.value) {
        koin.get<T>(qualifier, parameters)
    }*/
}
