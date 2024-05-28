package com.singing.app.ui.navigation

import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.model.NavigationItem
import com.singing.app.presenter.generated.resources.*
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


val NavigationItems = persistentListOf(
    NavigationItem(
        title = { stringResource(Res.string.title_home) },
        icon = { vectorResource(Res.drawable.baseline_dashboard_24) },
        reference = { SharedScreen.Main },
    ),
    NavigationItem(
        title = { stringResource(Res.string.title_records) },
        icon = { vectorResource(Res.drawable.baseline_album_24) },
        reference = { SharedScreen.RecordList() }
    ),
    NavigationItem(
        title = { stringResource(Res.string.title_community) },
        icon = { vectorResource(Res.drawable.baseline_explore_24) },
        reference = { SharedScreen.Community }
    ),
)

