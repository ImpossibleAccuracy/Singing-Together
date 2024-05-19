package org.singing.app.ui.screens.record.create.save.items

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.getKoin
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.repository.record.RecordSaveData
import org.singing.app.ui.screens.record.create.save.RecordSaveDialogScreen
import org.singing.app.ui.screens.record.create.save.RecordSavedScreen
import org.singing.app.ui.views.base.list.Loader

sealed interface RecordSaveStrategy {
    data object Locally : RecordSaveStrategy

    data class Remote(val account: AccountUiData) : RecordSaveStrategy
}

data class ProcessRecordSaveScreen(
    val saveData: RecordSaveData,
    val strategy: RecordSaveStrategy,
) : RecordSaveDialogScreen {
    @Composable
    override fun Content() {
        val koin = getKoin()
        val navigator = LocalNavigator.currentOrThrow

        val recordRepository = remember { koin.get<RecordRepository>() }

        LaunchedEffect(Unit) {
            val newRecord = recordRepository.saveRecord(
                data = saveData,
                saveRemote = strategy is RecordSaveStrategy.Remote,
            )

            navigator.replace(
                RecordSavedScreen(
                    record = newRecord,
                    info = strategy,
                )
            )
        }

        Loader(
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
