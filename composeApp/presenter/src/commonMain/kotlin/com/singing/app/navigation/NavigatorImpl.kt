package com.singing.app.navigation

import cafe.adriel.voyager.core.screen.Screen
import com.singing.app.feature.community.CommunityPage
import com.singing.feature.account.profile.AccountProfilePage
import com.singing.feature.main.MainPage
import com.singing.feature.record.RecordDetailPage
import com.singing.feature.record.list.RecordListPage
import com.singing.feature.recording.RecordingPage


class NavigatorImpl(
    private val navigator: cafe.adriel.voyager.navigator.Navigator,
) : Navigator {
    companion object {
        fun getScreenByPage(page: Screen): SharedScreen =
            when (page) {
                is MainPage -> SharedScreen.Main
                is CommunityPage -> SharedScreen.Community
                is RecordListPage -> SharedScreen.RecordList(page.initialRecord)
                is RecordDetailPage -> SharedScreen.RecordDetails(page.recordData)
                is AccountProfilePage -> SharedScreen.UserProfile(page.account)
                is RecordingPage -> SharedScreen.Recording(page.audio, page.isNewInstance)
                else -> TODO()
            }

        fun getPageByScreen(screen: SharedScreen): Screen =
            when (screen) {
                SharedScreen.Main -> MainPage()
                SharedScreen.Community -> CommunityPage()
                is SharedScreen.RecordList -> RecordListPage(screen.record)
                is SharedScreen.RecordDetails -> RecordDetailPage(screen.record)
                is SharedScreen.UserProfile -> AccountProfilePage(screen.account)
                is SharedScreen.Recording -> RecordingPage(screen.audio, screen.isNewInstance)
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
