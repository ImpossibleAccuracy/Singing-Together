package com.singing.app.navigation

import androidx.compose.runtime.staticCompositionLocalOf

val AppNavigator = staticCompositionLocalOf<Navigator?> {
    null
}

interface Navigator {
    val currentScreen: SharedScreen?

    fun navigate(screen: SharedScreen)

    fun replace(screen: SharedScreen)

    fun pop()

    fun popToRoot()
}
