package com.singing.feature.recording.save.items

import androidx.compose.runtime.Composable
import com.singing.app.domain.model.RecordData
import com.singing.app.navigation.dialog.FinalNavigationalDialogScreen
import com.singing.feature.recording.save.RecordSaveAdditionalInfo
import com.singing.feature.recording.save.RecordSaveStrategy


data class RecordSaveErrorScreen(
    val data: RecordSaveAdditionalInfo,
    val strategy: RecordSaveStrategy,
) : FinalNavigationalDialogScreen<RecordData?>() {
    override val result: RecordData?
        get() = null

    @Composable
    override fun Content() {
    }
}
