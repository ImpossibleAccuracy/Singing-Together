package com.singing.api.service.record

import com.singing.api.domain.model.AccountEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.api.domain.repository.RecordRepository
import com.singing.api.domain.specifications.*
import com.singing.api.security.getAuthentication
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class RecordServiceImpl(
    private val recordRepository: RecordRepository,
) : RecordService {
    override suspend fun save(record: RecordEntity): RecordEntity =
        recordRepository.save(record)

    override suspend fun get(recordId: Int): Optional<RecordEntity> =
        recordRepository.findById(recordId)

    override suspend fun publicRecords(onlyPublished: Boolean): List<RecordEntity> {
        if (!onlyPublished) return recordRepository.findAll()

        val onlyPublishedSpec = RecordEntity::publications.isNotEmpty()

        val accountOwnRecordsSpec = getAuthentication()?.account?.let {
            where<RecordEntity> { root ->
                equal(
                    root.join(RecordEntity::account).get(AccountEntity::id),
                    it.id,
                )
            }
        }

        return recordRepository.findAll(
            or(
                onlyPublishedSpec,
                accountOwnRecordsSpec
            ),
            Sort.by(Sort.Direction.DESC, RecordEntity::createdAt.name)
        )
    }

    override suspend fun accountRecords(accountId: Int): List<RecordEntity> =
        recordRepository.findByAccount_Id(
            id = accountId,
            sort = Sort.by(Sort.Direction.DESC, RecordEntity::createdAt.name)
        )

    override suspend fun delete(recordId: Int) =
        recordRepository.deleteById(recordId)
}
