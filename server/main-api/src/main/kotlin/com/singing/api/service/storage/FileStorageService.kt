package com.singing.api.service.storage

import com.singing.api.domain.model.DocumentEntity
import org.springframework.web.multipart.MultipartFile
import java.io.File

interface FileStorageService {

    /**
     * Searches for a file in the database by hash.
     *
     * If the file is not found, saves file to the [StorageCatalog.Regular] catalog and creates record in database.
     *
     * @param file File to save
     * @return File stored in specified catalog
     */
    fun findDocumentOrCreate(file: File): DocumentEntity

    /**
     * Creates [DocumentEntity] from file.
     */
    fun buildDocument(file: File): DocumentEntity

    /**
     * Saves the file in the specified catalog
     *
     * @param file File to save
     * @param catalog File destination
     * @return File stored in specified catalog
     */
    fun store(file: MultipartFile, catalog: StorageCatalog): File

    /**
     * Moves file from source catalog to destination catalog
     *
     * @param file File to save
     * @param catalog File destination catalog
     * @return File stored in destination catalog
     */
    fun move(file: File, catalog: StorageCatalog): File

    /**
     * Deletes file from catalog
     *
     * @param file File to delete
     * @param catalog File storage catalog
     * @return  true when the file successfully deleted;
     *          false if fails
     */
    fun delete(file: File, catalog: StorageCatalog): Boolean

    /**
     * Clear specified catalog
     *
     * @param catalog Catalog to clear
     */
    fun clearCatalog(catalog: StorageCatalog)
}
