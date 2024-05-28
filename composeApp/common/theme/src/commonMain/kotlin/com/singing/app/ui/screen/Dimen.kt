package com.singing.app.ui.screen

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val LocalDimensions = compositionLocalOf { mediumDimens }

@Suppress("PropertyName")
data class AppDimens(
    val dimen0_25: Dp,
    val dimen0_5: Dp,
    val dimen1: Dp,
    val dimen1_5: Dp,
    val dimen2: Dp,
    val dimen2_5: Dp,
    val dimen3: Dp,
    val dimen3_5: Dp,
    val dimen4: Dp,
    val dimen4_5: Dp,
    val dimen5: Dp,
    val dimen5_5: Dp,
    val dimen6: Dp,
    val bordersThickness: Dp,
)

inline val AppDimens.icon
    get() = dimen3

inline val AppDimens.largeIcon
    get() = dimen4_5

inline val AppDimens.smallListSpacing
    get() = dimen1

inline val AppDimens.listSpacing
    get() = dimen1_5

val compactDimens = AppDimens(
    dimen0_25 = 1.5f.dp,
    dimen0_5 = 3.dp,
    dimen1 = 6.dp,
    dimen1_5 = 9.dp,
    dimen2 = 12.dp,
    dimen2_5 = 15.dp,
    dimen3 = 18.dp,
    dimen3_5 = 21.dp,
    dimen4 = 23.dp,
    dimen4_5 = 26.dp,
    dimen5 = 29.dp,
    dimen5_5 = 32.dp,
    dimen6 = 35.dp,
    bordersThickness = 1.dp,
)

val mediumDimens = AppDimens(
    dimen0_25 = 2.dp,
    dimen0_5 = 4.dp,
    dimen1 = 8.dp,
    dimen1_5 = 12.dp,
    dimen2 = 16.dp,
    dimen2_5 = 20.dp,
    dimen3 = 24.dp,
    dimen3_5 = 28.dp,
    dimen4 = 32.dp,
    dimen4_5 = 36.dp,
    dimen5 = 40.dp,
    dimen5_5 = 44.dp,
    dimen6 = 48.dp,
    bordersThickness = 1.dp,
)

val expandedDimens = AppDimens(
    dimen0_25 = 2.5f.dp,
    dimen0_5 = 5.dp,
    dimen1 = 10.dp,
    dimen1_5 = 15.dp,
    dimen2 = 20.dp,
    dimen2_5 = 25.dp,
    dimen3 = 30.dp,
    dimen3_5 = 35.dp,
    dimen4 = 40.dp,
    dimen4_5 = 45.dp,
    dimen5 = 50.dp,
    dimen5_5 = 55.dp,
    dimen6 = 60.dp,
    bordersThickness = 1.5f.dp,
)
