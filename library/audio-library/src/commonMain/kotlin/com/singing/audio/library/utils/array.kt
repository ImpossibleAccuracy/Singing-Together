package com.singing.audio.library.utils

fun <T : Comparable<T>> Array<T>.indexOfMax(): Int {
    if (isEmpty()) return -1

    if (size == 1) return 0

    var pos = 0
    var max = get(0)

    for (i in 1..<size) {
        val item = get(i)
        if (item > max) {
            pos = i
            max = item
        }
    }

    return pos
}
