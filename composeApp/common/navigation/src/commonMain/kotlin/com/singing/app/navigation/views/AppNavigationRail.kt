package com.singing.app.navigation.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.navigation.model.NavigationItem
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AppNavigationRail(
    action: @Composable () -> Unit,
    fab: (@Composable () -> Unit)? = null,
    isSelected: (NavigationItem) -> Boolean,
    items: ImmutableList<NavigationItem>,
    navigate: (NavigationItem) -> Unit,
) {
    NavigationRail(
        modifier = Modifier
            .requiredWidth(80.dp)
            .padding(
                top = 44.dp,
                bottom = 56.dp,
            )
    ) {
        action()

        fab?.invoke()

        if (fab != null) {
            Spacer(Modifier.height(40.dp))
        } else {
            Spacer(Modifier.height(100.dp))
        }

        items.forEach {
            NavigationRailItem(
                selected = isSelected(it),
                label = { it.title() },
                icon = {
                    it.icon()
                },
                onClick = {
                    navigate(it)
                }
            )
        }
    }
}
