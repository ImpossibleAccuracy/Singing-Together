package com.singing.audio.library.transformation.fft

import kotlin.math.cos
import kotlin.math.sin

class KotlinFFT(numberOfSamples: Int) {
    companion object {
        private val FFT_BIT_TABLE = arrayOfNulls<IntArray>(16)

        private fun getNumberOfNeededBits(powerOfTwo: Int): Int {
            var i = 0

            while (true) {
                val j = powerOfTwo and (1 shl i)
                if (j != 0) {
                    return i
                }

                ++i
            }
        }

        private fun reverseBits(index: Int, numberOfBits: Int): Int {
            var ind = index
            var rev = 0

            for (i in 0 until numberOfBits) {
                rev = rev shl 1 or (ind and 1)
                ind = ind shr 1
            }

            return rev
        }

        private fun fastReverseBits(index: Int, numberOfBits: Int): Int {
            return if (numberOfBits <= 16) FFT_BIT_TABLE[numberOfBits - 1]!![index] else reverseBits(
                index,
                numberOfBits
            )
        }

        private fun isPowerOfTwo(number: Int): Boolean {
            return (number and number - 1) == 0
        }

        init {
            var len = 2

            for (b in 1..16) {
                FFT_BIT_TABLE[b - 1] = IntArray(len)

                for (i in 0 until len) {
                    FFT_BIT_TABLE[b - 1]!![i] = reverseBits(i, b)
                }

                len = len shl 1
            }
        }
    }

    private var numberOfSamples = 0
    private val reverseIndices: IntArray
    private val frequencies: FloatArray

    init {
        require(isPowerOfTwo(numberOfSamples)) {
            "N is not a power of 2"
        }

        require(numberOfSamples > 0) { "N must be greater than 0" }

        this.numberOfSamples = numberOfSamples

        val numberOfBits = getNumberOfNeededBits(numberOfSamples)
        this.reverseIndices = IntArray(numberOfSamples)

        var index = 0
        while (index < numberOfSamples) {
            val j = fastReverseBits(index, numberOfBits)
            reverseIndices[index] = j
            ++index
        }

        this.frequencies = FloatArray(numberOfSamples)

        index = 0
        while (index < numberOfSamples) {
            if (index <= numberOfSamples / 2) {
                frequencies[index] = (index / numberOfSamples).toFloat()
            } else {
                frequencies[index] = -((numberOfSamples - index).toFloat() / numberOfSamples.toFloat())
            }
            ++index
        }
    }

    @Throws(UnsupportedOperationException::class)
    fun inverseTransform(real: FloatArray, imaginary: FloatArray): Array<FloatArray> {
        val out = Array(2) { FloatArray(real.size) }
        this.transform(true, real, imaginary, out[0], out[1])
        return out
    }

    @Throws(UnsupportedOperationException::class)
    fun transform(real: FloatArray): Array<FloatArray> {
        val out = Array(3) { FloatArray(real.size) }
        this.transform(false, real, null as FloatArray?, out[0], out[1])
        out[2] = frequencies.clone()
        return out
    }

    @Throws(UnsupportedOperationException::class)
    fun transform(real: FloatArray, imaginary: FloatArray): Array<FloatArray> {
        val out = Array(3) { FloatArray(real.size) }
        this.transform(false, real, imaginary, out[0], out[1])
        out[2] = frequencies.clone()
        return out
    }

    fun transform(
        inverse: Boolean,
        realIn: FloatArray,
        imaginaryIn: FloatArray?,
        realOut: FloatArray,
        imaginaryOut: FloatArray
    ) {
        require(realIn.size == this.numberOfSamples) { "Number of samples must be " + this.numberOfSamples + " for this instance of JavaFFT" }
        var blockEnd: Int
        blockEnd = 0
        while (blockEnd < this.numberOfSamples) {
            realOut[reverseIndices[blockEnd]] = realIn[blockEnd]
            ++blockEnd
        }

        if (imaginaryIn != null) {
            blockEnd = 0
            while (blockEnd < this.numberOfSamples) {
                imaginaryOut[reverseIndices[blockEnd]] = imaginaryIn[blockEnd]
                ++blockEnd
            }
        }

        blockEnd = 1
        val angleNumerator = if (inverse) {
            -6.283185307179586
        } else {
            6.283185307179586
        }

        var blockSize: Int
        blockSize = 2
        while (blockSize <= this.numberOfSamples) {
            val deltaAngle = angleNumerator / blockSize.toFloat().toDouble()
            val sm2 = -sin(-2.0 * deltaAngle)
            val sm1 = -sin(-deltaAngle)
            val cm2 = cos(-2.0 * deltaAngle)
            val cm1 = cos(-deltaAngle)
            val w = 2.0 * cm1

            var i = 0
            while (i < this.numberOfSamples) {
                var ar2 = cm2
                var ar1 = cm1
                var ai2 = sm2
                var ai1 = sm1
                var j = i

                for (n in 0 until blockEnd) {
                    val ar0 = w * ar1 - ar2
                    ar2 = ar1
                    ar1 = ar0
                    val ai0 = w * ai1 - ai2
                    ai2 = ai1
                    ai1 = ai0
                    val k = j + blockEnd
                    val tr = ar0 * realOut[k].toDouble() - ai0 * imaginaryOut[k].toDouble()
                    val ti = ar0 * imaginaryOut[k].toDouble() + ai0 * realOut[k].toDouble()
                    realOut[k] = (realOut[j].toDouble() - tr).toFloat()
                    imaginaryOut[k] = (imaginaryOut[j].toDouble() - ti).toFloat()
                    realOut[j] = (realOut[j].toDouble() + tr).toFloat()
                    imaginaryOut[j] = (imaginaryOut[j].toDouble() + ti).toFloat()
                    ++j
                }
                i += blockSize
            }

            blockEnd = blockSize
            blockSize = blockSize shl 1
        }

        if (inverse) {
            blockSize = 0
            while (blockSize < this.numberOfSamples) {
                realOut[blockSize] /= numberOfSamples.toFloat()
                imaginaryOut[blockSize] /= numberOfSamples.toFloat()
                ++blockSize
            }
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        } else if (o != null && this.javaClass == o.javaClass) {
            val kotlinFFT = o as KotlinFFT
            return this.numberOfSamples == kotlinFFT.numberOfSamples
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return this.numberOfSamples
    }

    override fun toString(): String {
        return "JavaFFT{N=" + this.numberOfSamples + '}'
    }
}
