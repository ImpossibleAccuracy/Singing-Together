package com.singing.app.data.repository

import com.singing.app.domain.repository.NoteRepository
import com.singing.config.note.NotesStore

class NoteRepositoryImpl : NoteRepository {
    override fun findNote(frequency: Double): String {
        return NotesStore.findNote(frequency)
    }
}
