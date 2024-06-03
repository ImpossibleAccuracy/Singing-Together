package com.singing.feature.recording.save.items

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.koin.koinScreenModel
import com.singing.app.common.views.base.list.Loader
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.RecordData
import com.singing.app.navigation.dialog.NavigationalDialogScreen
import com.singing.feature.recording.save.RecordSaveAdditionalInfo
import com.singing.feature.recording.save.RecordSaveStrategy
import com.singing.feature.recording.save.RecordSaveViewModel


data class ProcessRecordSaveScreen(
    val data: RecordSaveAdditionalInfo,
    val strategy: RecordSaveStrategy,
) : NavigationalDialogScreen<RecordData?>() {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<RecordSaveViewModel>()

        LaunchedEffect(Unit) {
            val result = viewModel.save(data.saveData, strategy)

            if (result is DataState.Success) {
                navigate(RecordSavedScreen(data, strategy, result.data))
            } else {
                navigate(RecordSaveErrorScreen(data, strategy))
            }
        }

        Loader(
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
