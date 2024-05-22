package com.singing.api.test.record.save

import com.singing.api.domain.model.RecordEntity
import com.singing.api.domain.model.TestFile
import com.singing.api.domain.model.TestUser
import org.springframework.mock.web.MockMultipartFile
import java.io.File

data class RecordSaveRequestScope(
    val multipart: MockMultipartFile,
    val file: File,
    val entity: RecordEntity,
) {
    companion object {
        fun of(file: TestFile, user: TestUser, multipartName: String): RecordSaveRequestScope {
            val actualFile = file.get()

            val actualRecord = file.record(user)

            val multipartFile = MockMultipartFile(
                multipartName,
                actualFile.name,
                actualRecord.voiceRecord!!.type!!.mimeType,
                actualFile.readBytes()
            )

            return RecordSaveRequestScope(multipartFile, actualFile, actualRecord)
        }
    }
}
