package org.singing.app.domain.repository.record

import com.singing.app.domain.model.RecordPoint
import com.singing.audio.utils.ComposeFile
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.stable.StableInstant
import org.singing.app.domain.repository.StateRepository
import org.singing.app.domain.repository.publication.PublicationRepository
import org.singing.app.domain.repository.track.createTestFile
import kotlin.random.Random.Default.nextDouble
import kotlin.time.Duration.Companion.days

class RecordRepository(
    private val httpClient: HttpClient,
) : StateRepository<RecordData>(DefaultItems) {
    companion object {
        val DefaultItems: List<RecordData> =
            listOf(
                RecordData.Cover(
                    accuracy = 97,
                    name = "ASD.mp3",
                    duration = 133000,
                    createdAt = StableInstant(Clock.System.now().minus(19.days)),
                    isSavedRemote = true,
                    isPublished = true,
                    creatorId = 1,
                ),
                RecordData.Vocal(
                    duration = 10000,
                    createdAt = StableInstant(Clock.System.now().minus(7.days)),
                    isSavedRemote = true,
                    isPublished = false,
                    creatorId = 2,
                ),
                RecordData.Cover(
                    accuracy = 23,
                    name = "ASD.mp3",
                    duration = 10000,
                    createdAt = StableInstant(Clock.System.now().minus(12.days)),
                    isSavedRemote = true,
                    isPublished = false,
                    creatorId = 3,
                ),
                RecordData.Cover(
                    accuracy = 78,
                    name = "ASD.mp3",
                    duration = 10000,
                    createdAt = StableInstant(Clock.System.now().minus(3.days)),
                    isSavedRemote = true,
                    isPublished = true,
                    creatorId = 1,
                ),
                RecordData.Vocal(
                    duration = 10000,
                    createdAt = StableInstant(Clock.System.now().minus(23.days)),
                    isSavedRemote = false,
                    isPublished = false,
                    creatorId = 2,
                ),
            )
                .mapIndexed { index, item ->
                    when (item) {
                        is RecordData.Cover -> item.copy(isPublished = index < PublicationRepository.PublicationsCount)
                        is RecordData.Vocal -> item.copy(isPublished = index < PublicationRepository.PublicationsCount)
                    }
                }
    }

    suspend fun saveRecord(
        data: RecordSaveData,
        saveRemote: Boolean,
    ): RecordData = withContext(Dispatchers.IO) {
        if (data.track == null) {
            RecordData.Vocal(
                duration = 10000,
                createdAt = StableInstant(Clock.System.now()),
                isSavedRemote = saveRemote,
                isPublished = false,
                creatorId = 2,
            )
        } else {
            RecordData.Cover(
                accuracy = 78,
                name = data.track.name,
                duration = 10000,
                createdAt = StableInstant(Clock.System.now()),
                isSavedRemote = saveRemote,
                isPublished = true,
                creatorId = 1,
            )
        }.also(::addSingle)

        /*writeFile(
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
        println(response.bodyAsText())*/
    }

    fun getRecords(): Flow<List<RecordData>> = items
        .map { list ->
            list.sortedByDescending {
                it.createdAt.instant
            }
        }

    internal suspend fun markPublished(record: RecordData) =
        withContext(Dispatchers.IO) {
            updateSingle(record) {
                when (record) {
                    is RecordData.Cover -> record.copy(isPublished = true)
                    is RecordData.Vocal -> record.copy(isPublished = true)
                }
            }!!
        }

    suspend fun uploadRecord(record: RecordData): RecordData =
        withContext(Dispatchers.IO) {
            updateSingle(record) {
                when (record) {
                    is RecordData.Cover -> record.copy(isSavedRemote = true)
                    is RecordData.Vocal -> record.copy(isSavedRemote = true)
                }
            }!!
        }

    suspend fun deleteRecord(record: RecordData): Unit =
        withContext(Dispatchers.IO) {
            updateSingle(record) { null }
        }

    suspend fun getRecordPoints(record: RecordData): List<RecordPoint> =
        withContext(Dispatchers.IO) {
            val base = nextDouble(100.0, 250.0)

            val isCover = record is RecordData.Cover

            (0..20).map {
                RecordPoint(
                    time = it * 1000L,
                    first = base + it,
                    second = if (isCover) base + it + nextDouble(0.0, 10.0) else null,
                )
            }
        }

    suspend fun getRecordVoiceFile(record: RecordData): ComposeFile =
        withContext(Dispatchers.IO) {
            createTestFile().file
        }

    suspend fun getRecordTrackFile(record: RecordData): ComposeFile =
        withContext(Dispatchers.IO) {
            createTestFile().file
        }
}
