package org.singing.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.app_name
import com.singing.app.composeapp.generated.resources.title_record_screen
import org.jetbrains.compose.resources.stringResource
import org.singing.app.ui.screens.main.MainScreen
import org.singing.app.ui.screens.record.create.RecordingScreen
import org.singing.app.ui.screens.record.create.SelectAudioScreen
import org.singing.app.ui.screens.record.create.SelectRecordTypeScreen

@Composable
internal actual fun getScreenTitle(screen: Screen): String =
    when (screen) {
        is MainScreen -> stringResource(Res.string.app_name)
        is SelectRecordTypeScreen -> stringResource(Res.string.title_record_screen)
        is SelectAudioScreen -> stringResource(Res.string.title_record_screen)
        is RecordingScreen -> stringResource(Res.string.title_record_screen)

        else -> screen.key
    }

@Composable
internal actual fun isRootNavigationItem(screen: Screen): Boolean =
    when (screen) {
        is MainScreen, is RecordingScreen -> true
        else -> false
    }

@Composable
internal actual fun getTopAppBarColors(screen: Screen): Pair<Color, Color>? =
    when (screen) {
        is SelectRecordTypeScreen, is SelectAudioScreen ->
            MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer

        else -> null
    }
