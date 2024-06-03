package com.singing.app.navigation

import cafe.adriel.voyager.core.screen.Screen
import com.singing.feature.account.profile.AccountProfilePage
import com.singing.feature.auth.AuthPage
import com.singing.feature.community.CommunityPage
import com.singing.feature.main.MainPage
import com.singing.feature.publication.details.PublicationDetailsPage
import com.singing.feature.record.RecordDetailPage
import com.singing.feature.record.list.RecordListPage
import com.singing.feature.recording.RecordingPage
import com.singing.feature.recording.setup.SelectAudioPage
import com.singing.feature.recording.setup.extra.SelectRecordTypePage


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
                is SelectRecordTypePage -> SharedScreen.SelectRecordingType
                is SelectAudioPage -> SharedScreen.SelectRecordingAudio
                is PublicationDetailsPage -> SharedScreen.PublicationDetails(page.publication)
                is AuthPage -> SharedScreen.Auth
                else -> TODO()
            }

        fun getPageByScreen(screen: SharedScreen): Screen =
            when (screen) {
                SharedScreen.Main -> MainPage()
                SharedScreen.Auth -> AuthPage()
                SharedScreen.Community -> CommunityPage()
                is SharedScreen.RecordList -> RecordListPage(screen.record)
                is SharedScreen.RecordDetails -> RecordDetailPage(screen.record)
                is SharedScreen.UserProfile -> AccountProfilePage(screen.account)
                is SharedScreen.Recording -> RecordingPage(screen.audio, screen.isNewInstance)
                is SharedScreen.SelectRecordingAudio -> SelectAudioPage()
                is SharedScreen.SelectRecordingType -> SelectRecordTypePage()
                is SharedScreen.PublicationDetails -> PublicationDetailsPage(screen.publication)
            }
    }

    override val currentScreen: SharedScreen?
        get() = navigator.lastItemOrNull?.let { getScreenByPage(it) }

    override fun navigate(screen: SharedScreen) {
        val page = getPageByScreen(screen)
        navigator.push(page)
    }

    override fun replace(screen: SharedScreen) {
        val page = getPageByScreen(screen)
        navigator.replace(page)
    }

    override fun backOrReplace(screen: SharedScreen) {
        if (navigator.canPop) {
            pop()
        } else {
            navigate(screen)
        }
    }

    override fun pop() {
        navigator.pop()
    }

    override fun popToRoot() {
        navigator.popUntilRoot()
    }
}
