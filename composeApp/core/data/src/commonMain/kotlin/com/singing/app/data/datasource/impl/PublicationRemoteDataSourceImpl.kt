package com.singing.app.data.datasource.impl

import com.singing.app.data.datasource.declaration.PublicationDataSource
import com.singing.app.data.datasource.impl.api.ApiScheme
import com.singing.app.data.datasource.utils.DataMapper
import com.singing.app.data.datasource.utils.authHeader
import com.singing.app.data.datasource.utils.mapBody
import com.singing.app.data.datasource.utils.requireAuth
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.domain.payload.PublicationSearchFilters
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.provider.currentUser
import com.singing.domain.model.PublicationSort
import com.singing.domain.payload.dto.CategoryInfoDto
import com.singing.domain.payload.dto.PublicationDto
import com.singing.domain.payload.request.PublishRecordRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import pro.respawn.apiresult.ApiResult

class PublicationRemoteDataSourceImpl(
    private val userProvider: UserProvider,
    private val httpClient: HttpClient,
    private val publicationDataMapper: DataMapper<PublicationDto, Publication>,
    private val publicationTagInfoDataMapper: DataMapper<CategoryInfoDto, PublicationTagStatistics>,
) : PublicationDataSource.Remote {
    override suspend fun create(
        recordId: Int,
        description: String,
        tags: List<String>
    ): ApiResult<Publication> = ApiResult {
        userProvider.requireAuth()

        httpClient
            .post(ApiScheme.Publication.PublishRecord) {
                authHeader(userProvider)

                setBody(
                    PublishRecordRequest(
                        recordId = recordId,
                        description = description,
                        tags = tags,
                    )
                )
            }
            .mapBody(publicationDataMapper)
    }

    override suspend fun search(
        page: Int,
        filters: PublicationSearchFilters
    ): ApiResult<List<Publication>> = ApiResult {
        httpClient
            .get(ApiScheme.Publication.Search) {
                authHeader(userProvider)

                parameter("page", page)
                filters.tags?.let { parameter("tags", it) }
                filters.description?.let { parameter("description", it) }
                parameter("showOwnPublications", filters.showOwnPublications)
                parameter("sort", filters.sort)
            }
            .body<List<PublicationDto>>()
            .map(publicationDataMapper::map)
    }

    override suspend fun fetchLatestOwned(): ApiResult<List<Publication>> = ApiResult {
        userProvider.requireAuth()

        httpClient
            .get(
                ApiScheme.Publication.AccountPublications(
                    userProvider.currentUser!!.id
                )
            ) {
                authHeader(userProvider)

                parameter("sort", PublicationSort.DateCreated)
            }
            .body<List<PublicationDto>>()
            .map(publicationDataMapper::map)
    }

    override suspend fun fetchByUser(page: Int, userId: Int): ApiResult<List<Publication>> =
        ApiResult {
            httpClient
                .get(
                    ApiScheme.Publication.AccountPublications(userId)
                ) {
                    authHeader(userProvider)

                    parameter("page", page)
                    parameter("sort", PublicationSort.DateCreated)
                }
                .body<List<PublicationDto>>()
                .map(publicationDataMapper::map)
        }

    override suspend fun fetchRandom(): ApiResult<Publication> = ApiResult {
        httpClient
            .get(ApiScheme.Publication.RandomPublication) {
                authHeader(userProvider)
            }
            .mapBody(publicationDataMapper)
    }

    override suspend fun fetchByRecord(recordId: Int): ApiResult<Publication> = ApiResult {
        httpClient
            .get(ApiScheme.Publication.RecordPublication(recordId)) {
                authHeader(userProvider)
            }
            .mapBody(publicationDataMapper)
    }

    override suspend fun fetchPopularTags(): ApiResult<List<PublicationTagStatistics>> = ApiResult {
        httpClient
            .get(ApiScheme.Publication.PopularTags) {
                authHeader(userProvider)
            }
            .body<List<CategoryInfoDto>>()
            .map(publicationTagInfoDataMapper::map)
    }
}