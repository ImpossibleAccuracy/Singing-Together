package com.singing.app.data.setup.file

import com.singing.app.base.ComposeFile
import com.singing.app.data.setup.PlatformInitParams
import java.io.InputStream

actual class FileStoreProperties

actual class FileStore actual constructor(
    init: PlatformInitParams,
    properties: FileStoreProperties,
) {
    actual fun storeFile(data: InputStream): ComposeFile {
        TODO("Not yet implemented")
    }

    actual fun copyToStore(data: ComposeFile): ComposeFile {
        TODO("Not yet implemented")
    }

    actual fun getFile(
        recordId: Int,
        type: String
    ): ComposeFile? {
        TODO("Not yet implemented")
    }

    actual fun createRecordTempFile(
        recordId: Int,
        type: String,
        extension: String,
        data: InputStream
    ): ComposeFile {
        TODO("Not yet implemented")
    }
}