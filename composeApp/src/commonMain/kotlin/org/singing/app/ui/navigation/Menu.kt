package org.singing.app.ui.navigation

import cafe.adriel.voyager.core.screen.Screen
import com.singing.app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.singing.app.ui.screens.community.CommunityScreen
import org.singing.app.ui.screens.main.MainScreen
import org.singing.app.ui.screens.record.list.RecordListScreen


data class NavigationItem(
    val title: StringResource,
    val icon: DrawableResource,
    val isInstance: (Screen) -> Boolean,
    val screenProvider: () -> Screen,
)

val NavigationItems = listOf(
    NavigationItem(
        title = Res.string.title_home,
        icon = Res.drawable.baseline_dashboard_24,
        isInstance = { it is MainScreen },
        screenProvider = { MainScreen() }
    ),
    NavigationItem(
        title = Res.string.title_records,
        icon = Res.drawable.baseline_album_24,
        isInstance = { it is RecordListScreen },
        screenProvider = { RecordListScreen() }
    ),
    NavigationItem(
        title = Res.string.title_community,
        icon = Res.drawable.baseline_explore_24,
        isInstance = { it is CommunityScreen },
        screenProvider = { CommunityScreen() }
    ),
)
