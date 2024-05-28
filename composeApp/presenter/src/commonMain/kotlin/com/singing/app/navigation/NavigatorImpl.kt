package com.singing.app.navigation

import cafe.adriel.voyager.core.screen.Screen
import com.singing.app.feature.community.CommunityPage
import com.singing.feature.main.MainPage


class NavigatorImpl(
    private val navigator: cafe.adriel.voyager.navigator.Navigator,
) : Navigator {
    companion object {
        fun getScreenByPage(page: Screen): SharedScreen =
            when (page) {
                is MainPage -> SharedScreen.Main
                is CommunityPage -> SharedScreen.Community
                else -> TODO()
            }

        fun getPageByScreen(screen: SharedScreen): Screen =
            when (screen) {
                SharedScreen.Main -> MainPage()
                SharedScreen.Community -> CommunityPage()
                else -> TODO()
            }
    }

    override val currentScreen: SharedScreen?
        get() = navigator.lastItemOrNull?.let { getScreenByPage(it) }

    override fun navigate(screen: SharedScreen) {
        val page = getPageByScreen(screen)
        navigator.push(page)
    }

    override fun pop() {
        navigator.pop()
    }

    override fun popToRoot() {
        navigator.popUntilRoot()
    }
}
