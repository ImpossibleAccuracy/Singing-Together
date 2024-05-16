package org.singing.app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

data class ExtendedMaterialTheme(
    val material: ColorScheme,
    val extended: ExtendedMaterialColors,
)

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

    val timeline: TimelineColors,
)

val LightColorScheme = ExtendedMaterialTheme(
    material = lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        secondary = secondaryLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        tertiary = tertiaryLight,
        onTertiary = onTertiaryLight,
        tertiaryContainer = tertiaryContainerLight,
        onTertiaryContainer = onTertiaryContainerLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        outline = outlineLight,
        outlineVariant = outlineVariantLight,
        scrim = scrimLight,
        inverseSurface = inverseSurfaceLight,
        inverseOnSurface = inverseOnSurfaceLight,
        inversePrimary = inversePrimaryLight,
        surfaceDim = surfaceDimLight,
        surfaceBright = surfaceBrightLight,
        surfaceContainerLowest = surfaceContainerLowestLight,
        surfaceContainerLow = surfaceContainerLowLight,
        surfaceContainer = surfaceContainerLight,
        surfaceContainerHigh = surfaceContainerHighLight,
        surfaceContainerHighest = surfaceContainerHighestLight,
    ),
    extended = ExtendedMaterialColors(
        primaryFixed = primaryFixedLight,
        onPrimaryFixed = onPrimaryFixedLight,
        primaryFixedDim = primaryFixedDimLight,
        onPrimaryFixedVariant = onPrimaryFixedVariantLight,
        secondaryFixed = secondaryFixedLight,
        onSecondaryFixed = onSecondaryFixedLight,
        secondaryFixedDim = secondaryFixedDimLight,
        onSecondaryFixedVariant = onSecondaryFixedVariantLight,
        tertiaryFixed = tertiaryFixedLight,
        onTertiaryFixed = onTertiaryFixedLight,
        tertiaryFixedDim = tertiaryFixedDimLight,
        onTertiaryFixedVariant = onTertiaryFixedVariantLight,
        mainBannerScrim = mainBannerScrimLight,
        timeline = TimelineColors(
            best = Color(0xFFA4EB31), // TODO: extract colors
            normal = Color(0xFFF5C415),
            bad = Color(0xFFF0380F),
            worst = Color(0xFFF00F0F),
            undefined = Color(0xFFF00F0F),
        ),
    ),
)

val DarkColorScheme = ExtendedMaterialTheme(
    material = darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        secondary = secondaryDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        tertiary = tertiaryDark,
        onTertiary = onTertiaryDark,
        tertiaryContainer = tertiaryContainerDark,
        onTertiaryContainer = onTertiaryContainerDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
        scrim = scrimDark,
        inverseSurface = inverseSurfaceDark,
        inverseOnSurface = inverseOnSurfaceDark,
        inversePrimary = inversePrimaryDark,
        surfaceDim = surfaceDimDark,
        surfaceBright = surfaceBrightDark,
        surfaceContainerLowest = surfaceContainerLowestDark,
        surfaceContainerLow = surfaceContainerLowDark,
        surfaceContainer = surfaceContainerDark,
        surfaceContainerHigh = surfaceContainerHighDark,
        surfaceContainerHighest = surfaceContainerHighestDark,
    ),
    extended = ExtendedMaterialColors(
        primaryFixed = primaryFixedDark,
        onPrimaryFixed = onPrimaryFixedDark,
        primaryFixedDim = primaryFixedDimDark,
        onPrimaryFixedVariant = onPrimaryFixedVariantDark,
        secondaryFixed = secondaryFixedDark,
        onSecondaryFixed = onSecondaryFixedDark,
        secondaryFixedDim = secondaryFixedDimDark,
        onSecondaryFixedVariant = onSecondaryFixedVariantDark,
        tertiaryFixed = tertiaryFixedDark,
        onTertiaryFixed = onTertiaryFixedDark,
        tertiaryFixedDim = tertiaryFixedDimDark,
        onTertiaryFixedVariant = onTertiaryFixedVariantDark,
        mainBannerScrim = mainBannerScrimDark,
        timeline = TimelineColors(
            best = Color(0xFFA4EB31), // TODO: extract colors
            normal = Color(0xFFF5C415),
            bad = Color(0xFFF0380F),
            worst = Color(0xFFF00F0F),
            undefined = Color(0xFFF00F0F),
        ),
    ),
)

val MaterialTheme.extended: ExtendedMaterialColors
    @Composable
    @ReadOnlyComposable
    get() = ExtendedColors.current.extended
