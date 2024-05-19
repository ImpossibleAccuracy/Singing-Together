package org.singing.app.ui.screens.record.create.save

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.action_confirm
import com.singing.app.composeapp.generated.resources.action_dismiss
import org.jetbrains.compose.resources.stringResource

interface RecordSaveDialogScreen : Screen {
    @Composable
    fun confirmText(): String {
        return stringResource(Res.string.action_confirm)
    }

    @Composable
    fun dismissText(): String {
        return stringResource(Res.string.action_dismiss)
    }
}

abstract class SkippableRecordSaveDialogScreen : RecordSaveDialogScreen {
    abstract fun buildNextPage(): RecordSaveDialogScreen
}