package com.singing.app.data.datasource.impl

import com.singing.app.data.datamapper.impl.map
import com.singing.app.data.datasource.declaration.RecordDataSource
import com.singing.app.data.datasource.declaration.RecordFileDataSource
import com.singing.app.data.datasource.impl.api.ApiScheme
import com.singing.app.data.datasource.utils.authHeader
import com.singing.app.data.datasource.utils.requireAuth
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.provider.UserProvider
import com.singing.domain.model.RecordPoint
import com.singing.domain.payload.dto.RecordDto
import com.singing.domain.payload.dto.RecordPointDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import pro.respawn.apiresult.ApiResult
import pro.respawn.apiresult.asResult
import pro.respawn.apiresult.orNull
import pro.respawn.apiresult.require

class RecordRemoteDataSourceImpl(
    private val localRecordDataSource: RecordDataSource.Local,
    private val recordFileDataSource: RecordFileDataSource,
    private val userProvider: UserProvider,
    private val httpClient: HttpClient,
) : RecordDataSource.Remote {
    override suspend fun uploadRecord(record: RecordData): ApiResult<RecordData> = ApiResult {
        if (record.isSavedRemote) return record.asResult

        userProvider.requireAuth()

        val voiceFile = recordFileDataSource.getRecordVoiceFile(record).require().result
        val trackFile = recordFileDataSource.getRecordTrackFile(record).orNull()

        httpClient
            .submitFormWithBinaryData(
                url = ApiScheme.Record.UploadRecord,
                formData = formData {
                    append("voice", voiceFile.readAll(), Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${voiceFile.name}")
                    })

                    if (trackFile != null) {
                        append("track", trackFile.readAll(), Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=${trackFile.name}")
                        })
                    }
                }
            ) {
                authHeader(userProvider)

                parameter("title", record.name)
            }
            .body<RecordDto>()
            .let {
                map(
                    it,
                    localRecordDataSource.getLocalIdByRemoteId(it.id!!)
                )
            }
    }

    override suspend fun deleteRecord(record: RecordData) {
        userProvider.requireAuth()

        httpClient
            .get(ApiScheme.Record.DeleteRecord(record.key.remoteId!!)) {
                authHeader(userProvider)
            }
    }

    override suspend fun getRecordPoints(
        page: Int,
        record: RecordData
    ): ApiResult<List<RecordPoint>> = ApiResult {
        httpClient
            .get(ApiScheme.Record.RecordPoints(record.key.remoteId!!)) {
                authHeader(userProvider)

                parameter("page", page)
            }
            .body<List<RecordPointDto>>()
            .map(::map)
    }
}