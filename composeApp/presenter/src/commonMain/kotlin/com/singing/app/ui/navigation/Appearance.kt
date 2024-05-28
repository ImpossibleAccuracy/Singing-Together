package com.singing.app.ui.navigation

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.singing.app.navigation.NavigatorImpl
import com.singing.app.navigation.SharedScreen


fun areScreensSame(page: Screen, sharedScreen: SharedScreen): Boolean =
    NavigatorImpl.getScreenByPage(page) == sharedScreen

val SharedScreen.screen: Screen
    get() = NavigatorImpl.getPageByScreen(this)


@Suppress("EmptyMethod", "EmptyMethod")
@Composable
expect fun NavigationAppearance(
    navigator: Navigator,
    content: @Composable BoxScope.() -> Unit
)
