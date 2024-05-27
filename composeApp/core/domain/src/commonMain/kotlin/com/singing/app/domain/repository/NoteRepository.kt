package com.singing.app.domain.repository

interface NoteRepository {
    fun findNote(frequency: Double): String
}
