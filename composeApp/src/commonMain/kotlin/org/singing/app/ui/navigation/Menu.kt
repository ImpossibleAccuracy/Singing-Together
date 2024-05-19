package org.singing.app.ui.navigation

import cafe.adriel.voyager.core.screen.Screen
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.app_name
import com.singing.app.composeapp.generated.resources.baseline_close_black_24dp
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
        title = Res.string.app_name,
        icon = Res.drawable.baseline_close_black_24dp,
        isInstance = { it is MainScreen },
        screenProvider = { MainScreen() }
    ),
    NavigationItem(
        title = Res.string.app_name,
        icon = Res.drawable.baseline_close_black_24dp,
        isInstance = { it is RecordListScreen },
        screenProvider = { RecordListScreen() }
    ),
    NavigationItem(
        title = Res.string.app_name,
        icon = Res.drawable.baseline_close_black_24dp,
        isInstance = { it is CommunityScreen },
        screenProvider = { CommunityScreen() }
    ),
)
