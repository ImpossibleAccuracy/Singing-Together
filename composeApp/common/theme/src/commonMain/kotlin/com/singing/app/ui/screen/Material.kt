package com.singing.app.ui.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

val MaterialTheme.actualScreenSize
    @Composable
    @ReadOnlyComposable
    get() = LocalWindowSize.current

val MaterialTheme.dimens
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current
