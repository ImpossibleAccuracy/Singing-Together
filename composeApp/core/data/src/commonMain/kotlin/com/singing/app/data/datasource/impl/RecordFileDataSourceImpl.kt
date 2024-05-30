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
import com.singing.app.domain.provider.UserProvider
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import pro.respawn.apiresult.ApiResult

class RecordFileDataSourceImpl(
    private val appDatabase: AppDatabase,
    private val httpClient: HttpClient,
    private val userProvider: UserProvider,
    private val fileStore: FileStore,
) : RecordFileDataSource {
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
        fileType: String
    ): ComposeFile {
        val bodyChannel =
            httpClient
                .get(url) {
                    authHeader(userProvider)
                }
                .bodyAsChannel()

        return fileStore.createRecordTempFile(
            recordId = recordId,
            type = fileType,
            data = bodyChannel.toInputStream()
        )
    }
}