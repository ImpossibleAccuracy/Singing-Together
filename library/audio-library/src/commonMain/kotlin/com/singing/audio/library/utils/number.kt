package com.singing.audio.library.utils

fun Int.isPowerOf2(): Boolean {
    return (this != 0) && (this and (this - 1)) == 0
}
