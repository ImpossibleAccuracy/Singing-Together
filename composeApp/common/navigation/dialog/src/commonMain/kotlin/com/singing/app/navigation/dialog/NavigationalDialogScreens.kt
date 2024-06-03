package com.singing.app.navigation.dialog

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

abstract class NavigationalDialogScreen<T> : Screen {
    internal var navigator: Navigator? = null

    fun navigate(screen: NavigationalDialogScreen<T>) {
        screen.navigator = navigator
        navigator?.replace(screen)
    }
}

abstract class SkippableNavigationalDialogScreen<T> : NavigationalDialogScreen<T>() {
    abstract fun buildNextPage(): NavigationalDialogScreen<T>
}

abstract class FinalNavigationalDialogScreen<T> : NavigationalDialogScreen<T>() {
    abstract val result: T
}
