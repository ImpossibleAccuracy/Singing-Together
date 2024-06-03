package com.singing.app.navigation.model

import androidx.compose.runtime.Composable
import com.singing.app.navigation.SharedScreen

data class NavigationItem(
    val title: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val reference: () -> SharedScreen,
)