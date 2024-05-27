package com.singing.app.navigation.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.singing.app.navigation.SharedScreen

data class NavigationItem(
    val title: @Composable () -> String,
    val icon: @Composable () -> ImageVector,
    val reference: () -> SharedScreen,
)