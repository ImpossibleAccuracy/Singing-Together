package com.singing.audio.capture

import be.tarsos.dsp.writer.WaveHeader
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import xt.audio.Enums.XtSample
import xt.audio.Enums.XtSetup
import xt.audio.Structs.*
import xt.audio.XtAudio
import xt.audio.XtSafeBuffer
import xt.audio.XtStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.lang.Thread.sleep

actual class AudioCapture {
    companion object {
        private const val DEFAULT_SAMPLE_RATE = 41400

        private const val SAMPLE_RATE_BITS = 24
        private const val INPUT_CHANNELS = 1

        private fun getXtSamples(sampleSizeInBits: Int): XtSample =
            when (sampleSizeInBits) {
                8 -> XtSample.UINT8
                16 -> XtSample.INT16
                24 -> XtSample.INT24
                32 -> XtSample.INT32
                else -> throw IllegalArgumentException("Sample size in bits must be one of (8, 16, 24, 32)")
            }

        private fun wavDataSize(pcm: ByteArray): Int {
            return pcm.size + 44
        }
    }

    private val mix = XtMix(DEFAULT_SAMPLE_RATE, getXtSamples(SAMPLE_RATE_BITS))
    private val channels = XtChannels(INPUT_CHANNELS, 0, 0, 0)
    private val format = XtFormat(mix, channels)

    private val scope = CoroutineScope(Dispatchers.IO)

    private var deferred: Deferred<ByteArrayOutputStream>? = null

    private val isCapturing: Boolean
        get() = deferred != null

    private var isReadyToCapture = MutableStateFlow(false)


    actual suspend fun capture() {
        scope.launch {
            isReadyToCapture.value = false

            deferred = async {
                val capturedBytes = captureRawBytes()

                val resultStream = withContext(Dispatchers.IO) {
                    convertPcmToWav(capturedBytes)
                }

                resultStream
            }
        }
    }

    actual suspend fun awaitStart() {
        isReadyToCapture.first { it }
    }

    actual suspend fun stop(): ByteArray {
        val task = deferred ?: throw NullPointerException()

        deferred = null
        isReadyToCapture.value = false

        val stream = task.await()

        return stream.toByteArray()
    }


    private fun captureRawBytes(): ByteArray =
        XtAudio.init(null, null).use { platform ->
            val system = platform.setupToSystem(XtSetup.CONSUMER_AUDIO)

            val service = platform.getService(system)
                ?: throw RuntimeException("Cannot get service")

            val defaultInput = service.getDefaultDeviceId(false)
                ?: throw RuntimeException("Cannot get device")

            service.openDevice(defaultInput).use { device ->
                if (!device.supportsFormat(format)) {
                    throw RuntimeException("Format is not supported")
                }

                val size = device.getBufferSize(format)

                val streamParams = XtStreamParams(
                    true,
                    ::onBuffer,
                    null,
                    null
                )

                val deviceParams = XtDeviceStreamParams(streamParams, format, size.current)

                val outputStream = ByteArrayOutputStream()

                device.openStream(deviceParams, outputStream).use { stream ->
                    XtSafeBuffer.register(stream).use {
                        isReadyToCapture.value = true

                        stream.start()

                        while (isCapturing) {
                            sleep(10)
                        }

                        stream.stop()
                    }
                }

                outputStream.toByteArray()
            }
        }

    private fun onBuffer(stream: XtStream?, buffer: XtBuffer, user: Any): Int {
        val output = user as OutputStream
        val safe = XtSafeBuffer.get(stream)
        safe.lock(buffer)

        val input = getInputData(safe.input)
        val size = XtAudio.getSampleAttributes(mix.sample).size

        output.write(input, 0, buffer.frames * size)

        safe.unlock(buffer)
        return 0
    }

    private fun getInputData(audio: Any): ByteArray =
        when (audio) {
            is ByteArray -> audio
            else -> throw IllegalArgumentException("Unknown input class: ${audio.javaClass}")
        }

    private fun convertPcmToWav(
        data: ByteArray,
        out: ByteArrayOutputStream = ByteArrayOutputStream(
            wavDataSize(data)
        )
    ): ByteArrayOutputStream {
        val header = WaveHeader(
            WaveHeader.FORMAT_PCM,
            channels.inputs.toShort(), mix.rate, SAMPLE_RATE_BITS.toShort(), data.size
        )

        header.write(out)
        out.write(data)

        return out
    }
}
