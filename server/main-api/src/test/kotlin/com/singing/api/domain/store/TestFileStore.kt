package com.singing.api.domain.store

import com.singing.api.domain.model.*
import java.io.File

object TestFileStore {
    private const val PRESETS_DIR = "Files"

    private fun getResourceFile(path: String): File {
        val fullPath = PRESETS_DIR + File.separator + path

        val url = ClassLoader.getSystemResource(fullPath)

        return File(url.toURI())
    }

    val files = arrayOf(
        TestFile(
            get = {
                getResourceFile("regular${File.separator}100.wav")
            },
            mimeType = "audio/x-wave",
            record = {
                RecordEntity().apply {
                    author = it.account
                    accuracy = 123.0
                    duration = 10000
                    voiceRecord = DocumentEntity(
                        title = "ASDDSA",
                        hash = "rtio43890jfi34f403",
                        path = "regular${File.separator}100.wav",
                        type = DocumentTypeEntity(
                            title = "MOCK",
                            mimeType = "audio/x-wave"
                        ),
                    )
                    points = setOf(
                        RecordItemEntity(
                            time = 0,
                            frequency = 100.0,
                            trackFrequency = 100.0,
                        ),
                        RecordItemEntity(
                            time = 1000,
                            frequency = 150.0,
                            trackFrequency = 100.0,
                        ),
                        RecordItemEntity(
                            time = 3000,
                            frequency = 201.0,
                            trackFrequency = 100.0,
                        ),
                    )
                }
            },
        ),
        TestFile(
            get = {
                getResourceFile("regular${File.separator}440.wav")
            },
            mimeType = "audio/x-wave",
            record = {
                RecordEntity().apply {
                    author = it.account
                    accuracy = 98.0
                    duration = 1000
                    voiceRecord = DocumentEntity(
                        title = "OIPORITPO",
                        hash = "M<<CM)(@#><%>@#<MN$",
                        path = "regular${File.separator}440.wav",
                        type = DocumentTypeEntity(
                            title = "MOCK",
                            mimeType = "audio/x-wave"
                        ),
                    )
                    points = setOf(
                        RecordItemEntity(
                            time = 0,
                            frequency = 100.0,
                            trackFrequency = 100.0,
                        ),
                        RecordItemEntity(
                            time = 200,
                            frequency = 150.0,
                            trackFrequency = 100.0,
                        ),
                        RecordItemEntity(
                            time = 300,
                            frequency = 201.0,
                            trackFrequency = 100.0,
                        ),
                        RecordItemEntity(
                            time = 700,
                            frequency = 201.0,
                            trackFrequency = 100.0,
                        ),
                    )
                }
            },
        ),
        TestFile(
            get = {
                getResourceFile("regular${File.separator}100_mono.wav")
            },
            mimeType = "audio/x-wave",
            record = {
                RecordEntity().apply {
                    author = it.account
                    accuracy = 45.0
                    duration = 1000
                    voiceRecord = DocumentEntity(
                        title = "KLAJDASKLD",
                        hash = "M<<CM)(@#><)_@QKJMDPO(QDNCHJ@(D%>@#<MN$",
                        path = "regular${File.separator}100_mono.wav",
                        type = DocumentTypeEntity(
                            title = "MOCK",
                            mimeType = "audio/x-wave"
                        ),
                    )
                    points = setOf(
                        RecordItemEntity(
                            time = 0,
                            frequency = 100.0,
                            trackFrequency = 100.0,
                        ),
                        RecordItemEntity(
                            time = 300,
                            frequency = 201.0,
                            trackFrequency = 100.0,
                        ),
                        RecordItemEntity(
                            time = 700,
                            frequency = 201.0,
                            trackFrequency = 100.0,
                        ),
                    )
                }
            },
        ),
    )

    val unacceptableFiles = arrayOf(
        UnacceptableTestFile(
            get = {getResourceFile("unacceptable${File.separator}1.txt")},
            mimeType = "text/plain",
        ),
        UnacceptableTestFile(
            get = {getResourceFile("unacceptable${File.separator}2.mp4")},
            mimeType = "video/mp4",
        ),
        UnacceptableTestFile(
            get = {getResourceFile("unacceptable${File.separator}3.raw")},
            mimeType = "text/raw",
        ),
        UnacceptableTestFile(
            get = {getResourceFile("unacceptable${File.separator}4.exe")},
            mimeType = "application/x-msdownload",
        ),
        UnacceptableTestFile(
            get = {getResourceFile("unacceptable${File.separator}5.ico")},
            mimeType = "image/x-icon",
        ),
        UnacceptableTestFile(
            get = {getResourceFile("unacceptable${File.separator}6")},
            mimeType = null,
        ),
    )
}
