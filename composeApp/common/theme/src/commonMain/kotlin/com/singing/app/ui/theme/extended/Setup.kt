package com.singing.app.ui.theme.extended

import androidx.compose.ui.graphics.Color

data class TimelineColors(
    val best: Color,
    val normal: Color,
    val bad: Color,
    val worst: Color,
    val undefined: Color,
)

data class ExtendedMaterialColors(
    val primaryFixed: Color,
    val onPrimaryFixed: Color,
    val primaryFixedDim: Color,
    val onPrimaryFixedVariant: Color,

    val secondaryFixed: Color,
    val onSecondaryFixed: Color,
    val secondaryFixedDim: Color,
    val onSecondaryFixedVariant: Color,

    val tertiaryFixed: Color,
    val onTertiaryFixed: Color,
    val tertiaryFixedDim: Color,
    val onTertiaryFixedVariant: Color,

    val mainBannerScrim: Color,
    val onMainBannerScrim: Color,

    val communityBannerColor: Color,
    val onCommunityBannerColor: Color,

    val timeline: TimelineColors,
)
