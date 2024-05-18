package org.singing.app.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

@Composable
expect fun getStartDestination(): Screen
