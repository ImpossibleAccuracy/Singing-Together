package com.singing.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.singing.app.domain.provider.UserProvider
import com.singing.app.navigation.model.NavigationItem
import com.singing.app.ui.navigation.items.buildProfileMenuItem
import com.singing.app.ui.navigation.items.communityMenuItem
import com.singing.app.ui.navigation.items.homeMenuItem
import com.singing.app.ui.navigation.items.recordsMenuItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.getKoin

@Composable
fun NavigationItems(): PersistentList<NavigationItem> {
    val koin = getKoin()
    val userProvider = remember { koin.get<UserProvider>() }

    val user by userProvider.userFlow.collectAsState()

    return remember(user) {
        persistentListOf(
            homeMenuItem,
            buildProfileMenuItem(user),
            recordsMenuItem,
            communityMenuItem,
        )
    }
}
