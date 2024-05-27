package com.singing.app.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

@Composable
expect fun getStartDestination(): Screen

expect fun isRootScreen(screen: Screen): Boolean
