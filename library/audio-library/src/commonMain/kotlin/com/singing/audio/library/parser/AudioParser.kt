package com.singing.audio.library.parser

import kotlinx.coroutines.flow.Flow

interface AudioParser<T> {
    fun parse(): Flow<T>
}
