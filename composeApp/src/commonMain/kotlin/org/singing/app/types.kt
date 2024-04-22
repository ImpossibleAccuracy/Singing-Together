package org.singing.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

typealias ComposeViewWithModifier = @Composable (modifier: Modifier) -> Unit

typealias ComposeView = @Composable () -> Unit

