package com.singing.app.navigation.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import org.koin.core.parameter.parametersOf

val LocalAppPage = staticCompositionLocalOf<AppPage<*>?> {
    null
}

typealias BaseAppPage = Screen

abstract class AppPage<T : ScreenModel> : Screen {
    @Composable
    protected abstract fun rememberLocalModelFactory(): T

    @Composable
    override fun Content() {
        CompositionLocalProvider(LocalAppPage provides this) {
            val screenModel = rememberLocalModelFactory()

            Content(screenModel)
        }
    }

    @Composable
    protected abstract fun Content(screenModel: T)
}

@Composable
inline fun <reified T : ScreenModel> AppPage<T>.screenModel(
    vararg params: Any?
) = koinScreenModel<T> {
    parametersOf(*params)
}
