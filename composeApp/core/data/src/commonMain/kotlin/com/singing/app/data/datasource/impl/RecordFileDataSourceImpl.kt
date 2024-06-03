package com.singing.app.data.datasource.impl

import com.singing.app.base.ComposeFile
import com.singing.app.base.openFile
import com.singing.app.data.database.AppDatabase
import com.singing.app.data.datasource.declaration.RecordFileDataSource
import com.singing.app.data.datasource.impl.api.ApiScheme
import com.singing.app.data.datasource.utils.authHeader
import com.singing.app.data.setup.file.FileStore
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.isTwoLineRecord
import com.singing.app.domain.payload.RecordSaveData
import com.singing.app.domain.provider.UserProvider
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pro.respawn.apiresult.ApiResult
import java.io.ByteArrayInputStream

class RecordFileDataSourceImpl(
    private val appDatabase: AppDatabase,
    private val httpClient: HttpClient,
    private val userProvider: UserProvider,
    private val fileStore: FileStore,
) : RecordFileDataSource {
    override suspend fun storeVoiceFile(data: RecordSaveData): ComposeFile =
        withContext(Dispatchers.IO) {
            fileStore.storeFile(ByteArrayInputStream(data.record))
        }

    override suspend fun storeTrackFile(data: RecordSaveData): ComposeFile =
        withContext(Dispatchers.IO) {
            fileStore.copyToStore(data.track!!.file)
        }

    override suspend fun getRecordVoiceFile(record: RecordData): ApiResult<ComposeFile> =
        ApiResult {
            if (record.isSavedLocally) {
                val documentId = appDatabase.recordQueries
                    .selectVoiceRecordId(record.key.localId!!.toLong())
                    .executeAsOne()

                getRecordFileLocal(documentId)
            } else {
                getRecordVoiceFileRemote(
                    url = ApiScheme.Record.RecordVoiceFile(
                        record.key.remoteId!!
                    ),
                    recordId = record.key.remoteId!!,
                    fileType = "voice",
                )
            }
        }

    override suspend fun getRecordTrackFile(record: RecordData): ApiResult<ComposeFile> =
        ApiResult {
            if (!record.isTwoLineRecord()) {
                throw IllegalArgumentException("Track file unavailable because record is RecordData.Cover")
            }

            if (record.isSavedLocally) {
                val documentId = appDatabase.recordQueries
                    .selectTrackId(record.key.localId!!.toLong())
                    .executeAsOne()

                if (documentId.trackId == null) {
                    throw NullPointerException("Track document unavailable")
                } else {
                    getRecordFileLocal(documentId.trackId)
                }
            } else {
                getRecordVoiceFileRemote(
                    url = ApiScheme.Record.RecordTrackFile(
                        record.key.remoteId!!
                    ),
                    recordId = record.key.remoteId!!,
                    fileType = "track",
                )
            }
        }

    private fun getRecordFileLocal(documentId: Long): ComposeFile {
        val documentPath = appDatabase.documentQueries
            .selectPath(documentId)
            .executeAsOne()

        return openFile(documentPath)
    }

    private suspend fun getRecordVoiceFileRemote(
        url: String,
        recordId: Int,
        fileType: String,
    ): ComposeFile {
        val cache = fileStore.getFile(recordId, fileType)

        if (cache != null) {
            return cache
        }

        val response = httpClient.get(url) {
            authHeader(userProvider)
        }

        val contentDisposition = response.headers["Content-Disposition"]!!
        val fileName = contentDisposition.substringAfterLast("filename=")
        val fileExtension = fileName.substringAfterLast(".")

        val bodyChannel = response.bodyAsChannel()

        return fileStore.createRecordTempFile(
            recordId = recordId,
            type = fileType,
            extension = fileExtension,
            data = bodyChannel.toInputStream()
        )
    }
}