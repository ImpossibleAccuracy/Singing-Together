package com.singing.app.ui.navigation

import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.model.NavigationItem
import com.singing.app.presenter.generated.resources.Res
import com.singing.app.presenter.generated.resources.baseline_dashboard_24
import com.singing.app.presenter.generated.resources.title_home
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


val NavigationItems = persistentListOf(
    NavigationItem(
        title = { stringResource(Res.string.title_home) },
        icon = { vectorResource(Res.drawable.baseline_dashboard_24) },
        reference = { SharedScreen.Main },
    ),
//    NavigationItem(
//        title = Res.string.title_records,
//        icon = Res.drawable.baseline_album_24,
//        isInstance = { it is RecordListScreen },
//        screenProvider = { RecordListScreen() }
//    ),
//    NavigationItem(
//        title = Res.string.title_community,
//        icon = Res.drawable.baseline_explore_24,
//        isInstance = { it is CommunityScreen },
//        screenProvider = { CommunityScreen() }
//    ),
)

