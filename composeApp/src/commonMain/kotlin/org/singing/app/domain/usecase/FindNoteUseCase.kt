package org.singing.app.domain.usecase

import com.singing.config.note.NotesStore

class FindNoteUseCase {
    operator fun invoke(frequency: Double): String =
        NotesStore.findNote(frequency)
}
