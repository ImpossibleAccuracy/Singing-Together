package com.singing.app.data.setup.file

import com.singing.app.base.ComposeFile
import com.singing.app.data.setup.PlatformInitParams
import java.io.InputStream

expect class FileStoreProperties

expect class FileStore(init: PlatformInitParams, properties: FileStoreProperties) {
    fun storeFile(data: InputStream): ComposeFile

    fun copyToStore(data: ComposeFile): ComposeFile

    fun getFile(
        recordId: Int,
        type: String,
    ): ComposeFile?

    // TODO: cache files must be deleted after user leaves application
    fun createRecordTempFile(
        recordId: Int,
        type: String,
        extension: String,
        data: InputStream
    ): ComposeFile
}