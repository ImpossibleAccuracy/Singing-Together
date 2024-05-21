package com.singing.api.domain.repository.pagination

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.Serializable

class OffsetBasedPageRequest(
    private val offset: Long,
    private val limit: Int,
    private val sort: Sort,
) : Pageable, Serializable {
    override fun getPageNumber() = (offset / limit).toInt()

    override fun getPageSize() = limit

    override fun getOffset() = offset

    override fun getSort() = sort

    override fun first() = OffsetBasedPageRequest(0, limit, sort)

    override fun next() = OffsetBasedPageRequest(offset + limit, limit, sort)

    private fun previous() = OffsetBasedPageRequest(offset - limit, limit, sort)

    override fun previousOrFirst() =
        if (hasPrevious()) previous()
        else first()

    override fun withPage(pageNumber: Int) = OffsetBasedPageRequest((pageNumber * limit).toLong(), limit, sort)

    override fun hasPrevious() = offset > limit
}