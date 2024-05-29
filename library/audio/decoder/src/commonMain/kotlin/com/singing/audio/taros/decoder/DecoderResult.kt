package com.singing.audio.taros.decoder

sealed interface DecoderResult<out T> {
    data class Data<T>(val data: T) : DecoderResult<T>

    data object NoData : DecoderResult<Nothing>
}