package org.singing.app.domain.repository

import com.singing.audio.player.model.AudioFile
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singing.app.ui.screens.record.viewmodel.model.RecordPair
import java.nio.file.Path
import java.nio.file.Paths

class RecordRepository(
    private val httpClient: HttpClient,
) {
    suspend fun saveRecord(
        history: List<RecordPair>,
        voiceRecord: ByteArray,
        audioTrack: AudioFile? = null,
    ) {
        withContext(Dispatchers.IO) {
            writeFile(
                Paths.get("TestStore", "test.wav"),
                voiceRecord,
            )

            val response = httpClient.post {
                url("http://localhost:8000/record")
                contentType(ContentType.MultiPart.FormData)

                headers {
                    set("Authorization", "JWT_TOKEN")
                }

                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "voice",
                                value = voiceRecord,
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, "audio/wave")
                                    append(HttpHeaders.ContentDisposition, "filename=voice.wav")
                                }
                            )

//                            if (audioTrack != null) {
//                                append(
//                                    "track",
//                                    readFileBytes(audioTrack),
//                                    Headers.build {
//                                        append(HttpHeaders.ContentDisposition, "filename=${audioTrack.name}")
//                                    }
//                                )
//                            }
                        }
                    )
                )
            }

            println(response)
            println(response.bodyAsText())
        }
    }
}

expect fun readFileBytes(file: AudioFile): ByteArray

expect fun writeFile(filename: Path, data: ByteArray)
