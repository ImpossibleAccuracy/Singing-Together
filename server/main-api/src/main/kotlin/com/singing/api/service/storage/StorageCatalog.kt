package com.singing.api.service.storage

import java.nio.file.Path
import java.nio.file.Paths

enum class StorageCatalog(
    internal val path: (FileStorageProperties) -> Path
) {
    Temp(
        path = {
            Paths.get(it.tempStorePath)
        }
    ),

    Regular(
        path = {
            Paths.get(it.regularStorePath)
        }
    ),
}
