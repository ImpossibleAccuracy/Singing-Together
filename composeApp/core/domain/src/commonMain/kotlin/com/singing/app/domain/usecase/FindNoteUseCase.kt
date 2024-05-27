package com.singing.app.domain.usecase

import com.singing.app.domain.repository.NoteRepository

class FindNoteUseCase(
    private val noteRepository: NoteRepository,
) {
    operator fun invoke(frequency: Double): String =
        noteRepository.findNote(frequency)
}
