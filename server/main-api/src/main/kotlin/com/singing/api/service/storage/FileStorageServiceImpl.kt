package com.singing.api.service.storage

import com.singing.api.domain.model.DocumentEntity
import com.singing.api.domain.model.DocumentTypeEntity
import com.singing.api.domain.repository.DocumentRepository
import com.singing.api.domain.repository.DocumentTypeRepository
import com.singing.api.security.getAuthentication
import com.singing.api.service.storage.utils.replace
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.net.URLConnection
import java.nio.file.Files
import java.security.MessageDigest
import java.time.Instant
import java.time.LocalDateTime
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively

@Service
@OptIn(ExperimentalPathApi::class, ExperimentalStdlibApi::class)
class FileStorageServiceImpl(
    private val documentRepository: DocumentRepository,
    private val documentTypeRepository: DocumentTypeRepository,
    private val properties: FileStorageProperties,
) : FileStorageService {
    companion object {
        private val Logger = LoggerFactory.getLogger(FileStorageServiceImpl::class.java)

        private const val MAX_ORIGINAL_NAME_LENGTH = 15

        private const val FILE_NAME_PATTERN = "{FILE_NAME}"
        private const val FILE_HASH_PATTERN = "{FILE_HASH}"
        private const val TIMESTAMP_PATTERN = "{TIMESTAMP}"
        private const val USER_UPLOADED_PATTERN = "{USER_UPLOADED}"
        private const val EXTENSION_PATTERN = "{EXTENSION}"
    }

    init {
        StorageCatalog.entries.forEach {
            val catalogPath = it.path(properties)
            catalogPath.createDirectories()
        }
    }

    override fun findDocumentOrCreate(file: File): DocumentEntity {
        val hash = getFileHash(file.readBytes())

        return documentRepository.findByHash(hash).orElseGet {
            val storedFile = move(file, StorageCatalog.Regular)

            val document = buildDocument(storedFile)

            documentRepository.save(document)
        }
    }

    override fun buildDocument(file: File): DocumentEntity =
        DocumentEntity(
            createdAt = Instant.now(),
            title = substring(file.name, DocumentEntity.MAX_TITLE_LENGTH),
            hash = getFileHash(file.readBytes()),
            path = substring(file.absolutePath, DocumentEntity.MAX_PATH_LENGTH),
            type = getDocumentType(file),
        )

    private fun getFileHash(bytes: ByteArray): String {
        val md = MessageDigest.getInstance("MD5")

        val digest = md.digest(bytes)

        return digest.toHexString()
    }

    private fun substring(string: String, maxLength: Int): String =
        if (string.length <= maxLength) string
        else string.substring(0, maxLength)

    private fun getDocumentType(file: File): DocumentTypeEntity =
        URLConnection.guessContentTypeFromName(file.name).let { mimeType: String? ->
            val actualMimeType = mimeType ?: "audio/wav"

            documentTypeRepository.findByMimeType(actualMimeType)
                .orElseGet {
                    DocumentTypeEntity(
                        title = "From MIME type $actualMimeType",
                        mimeType = actualMimeType,
                    ).let(documentTypeRepository::save)
                }
        }


    override fun store(file: MultipartFile, catalog: StorageCatalog): File {
        val originalName = file.originalFilename ?: file.name
        val fileName = computeFileName(originalName, file.bytes)

        val catalogPath = catalog.path(properties)
        val destination = catalogPath.resolve(fileName)

        file.transferTo(destination)

        val resultFile = destination.toFile()

        Logger.info(
            "File ${cutFileName(originalName)} stored into $catalog catalog as ${resultFile.absoluteFile}"
        )

        return resultFile
    }

    private fun computeFileName(originalFileName: String, content: ByteArray): String {
        return buildString {
            append(properties.fileNamePattern)

            replace(FILE_NAME_PATTERN) {
                val string = originalFileName.substringBeforeLast(".")

                cutFileName(string)
            }

            replace(FILE_HASH_PATTERN) {
                getFileHash(content)
            }

            replace(TIMESTAMP_PATTERN) {
                LocalDateTime.now().toString()
            }

            replace(USER_UPLOADED_PATTERN) {
                getAuthentication()?.account?.username ?: "no_user_presented"
            }

            replace(EXTENSION_PATTERN) {
                originalFileName.substringAfterLast(".")
            }

            replace(":") { "-" }
        }
    }

    private fun cutFileName(fileName: String) =
        if (fileName.length > MAX_ORIGINAL_NAME_LENGTH) fileName.substring(0, MAX_ORIGINAL_NAME_LENGTH)
        else fileName

    override fun move(file: File, catalog: StorageCatalog): File {
        val catalogPath = catalog.path(properties)
        val filePath = file.toPath()

        if (filePath.contains(catalogPath)) {
            return file
        }

        val resultPath = Files.move(
            filePath,
            catalogPath.resolve(file.name),
        )

        val resultFile = resultPath.toFile()

        Logger.info(
            "File ${cutFileName(file.name)} moved into $catalog catalog as ${resultFile.absoluteFile}"
        )

        return resultFile
    }

    override fun delete(file: File, catalog: StorageCatalog): Boolean {
        val catalogPath = catalog.path(properties).toAbsolutePath()
        val filePath = file.toPath().toAbsolutePath()

        if (!filePath.startsWith(catalogPath)) {
            return false
        }

        return file.delete().also {
            if (it) {
                Logger.info("File ${cutFileName(file.name)} deleted from $catalog catalog")
            }
        }
    }

    override fun clearCatalog(catalog: StorageCatalog) {
        val catalogPath = catalog.path(properties)

        catalogPath.deleteRecursively()
    }
}
