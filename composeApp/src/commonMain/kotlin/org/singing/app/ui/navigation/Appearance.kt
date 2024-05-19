package org.singing.app.ui.navigation

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator


@Composable
expect fun NavigationAppearance(
    navigator: Navigator,
    content: @Composable BoxScope.() -> Unit,
)
