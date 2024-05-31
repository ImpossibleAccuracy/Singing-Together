package com.singing.app.data.setup.database

import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.singing.app.data.database.AppDatabase
import com.singing.app.data.setup.PlatformInitParams

actual data class DatabaseParameters(
    val name: String,
)

internal actual fun createDriver(
    init: PlatformInitParams,
    parameters: DatabaseParameters
): SqlDriver =
    AndroidSqliteDriver(
        schema = AppDatabase.Schema,
        context = init.context,
        name = parameters.name,
        callback = object : AndroidSqliteDriver.Callback(AppDatabase.Schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                db.setForeignKeyConstraintsEnabled(true)
            }
        }
    )
