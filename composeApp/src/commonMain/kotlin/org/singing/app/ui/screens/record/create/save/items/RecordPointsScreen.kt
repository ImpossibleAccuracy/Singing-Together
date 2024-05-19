package org.singing.app.ui.screens.record.create.save.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.singing.config.note.NotesStore
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.ui.screens.record.create.save.RecordSaveAdditionalInfo
import org.singing.app.ui.screens.record.create.save.SkippableRecordSaveDialogScreen
import org.singing.app.ui.views.base.record.RecordTimeline

data class RecordPointsScreen(
    val data: RecordSaveAdditionalInfo,
) : SkippableRecordSaveDialogScreen() {
    override fun buildNextPage() =
        if (UserContainer.user.value == null) ProcessRecordSaveScreen(
            saveData = data.saveData,
            strategy = RecordSaveStrategy.Locally
        )
        else SelectRecordStoreMethodScreen(data.saveData)

    @Composable
    override fun Content() {
        RecordTimeline(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            isLazyColumn = true,
            isTwoLineRecord = data.saveData.track != null,
            points = data.history,
            note = {
                NotesStore.findNote(it)
            }
        )
    }
}
