package com.singing.app.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.singing.feature.community.CommunityPage
import com.singing.feature.main.MainPage
import com.singing.feature.record.list.RecordListPage

@Composable
expect fun getStartDestination(): Screen

fun isRootScreen(screen: Screen): Boolean =
    screen is MainPage || screen is RecordListPage || screen is CommunityPage
