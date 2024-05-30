package com.singing.app.data.setup.database

import app.cash.sqldelight.db.SqlDriver
import com.singing.app.data.database.AppDatabase
import com.singing.app.data.setup.PlatformInitParams

expect class DatabaseParameters

internal expect fun createDriver(
    init: PlatformInitParams,
    parameters: DatabaseParameters
): SqlDriver

fun setupAppDatabase(init: PlatformInitParams, parameters: DatabaseParameters): AppDatabase {
    val driver = createDriver(init, parameters)

    return AppDatabase(driver)
}
