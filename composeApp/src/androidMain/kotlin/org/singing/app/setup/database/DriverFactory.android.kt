package org.singing.app.setup.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.singing.app.Database
import org.singing.app.MyApplication

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(
            schema = Database.Schema,
            context = MyApplication.appContext,
            name = "mylocaldb.db"
        )
}
