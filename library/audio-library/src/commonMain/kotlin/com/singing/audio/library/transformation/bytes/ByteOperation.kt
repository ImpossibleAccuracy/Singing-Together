package com.singing.audio.library.transformation.bytes

object ByteOperation {
    fun byteToIntLittleEndian(buf: ByteArray, offset: Int, bytesPerSample: Int): Int {
        var sample = 0
        for (byteIndex in 0 until bytesPerSample) {
            val aByte = buf[offset + byteIndex].toInt() and 0xff
            sample += aByte shl 8 * (byteIndex)
        }
        return sample
    }

    fun byteToIntBigEndian(buf: ByteArray, offset: Int, bytesPerSample: Int): Int {
        var sample = 0
        for (byteIndex in 0 until bytesPerSample) {
            val aByte = buf[offset + byteIndex].toInt() and 0xff
            sample += aByte shl (8 * (bytesPerSample - byteIndex - 1))
        }
        return sample
    }
}
