package com.singing.app.data.setup.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.singing.app.data.setup.PlatformInitParams
import java.util.Properties

actual class DatabaseParameters(
    val name: String,
)

internal actual fun createDriver(
    init: PlatformInitParams,
    parameters: DatabaseParameters
): SqlDriver =
    JdbcSqliteDriver(
        url = parameters.name,
        properties = Properties().apply { put("foreign_keys", "true") },
    )
