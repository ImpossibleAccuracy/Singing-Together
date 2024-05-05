package org.singing.app.setup.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.singing.app.Database
import java.io.File

actual class DatabaseDriverFactory {
    companion object {
        private const val DB_PATH = "MyApp"
        private const val DB_NAME = "mylocaldb"
    }

    actual fun createDriver(): SqlDriver {
        val isDebug = true

        val parentFolder = if (isDebug) {
            File(System.getProperty("java.io.tmpdir"))
        } else {
            File(System.getProperty("user.home") + DB_PATH)
        }

        if (!parentFolder.exists()) {
            parentFolder.mkdirs()
        }
        val databasePath = if (isDebug) {
            File(System.getProperty("java.io.tmpdir"), DB_NAME)
        } else {
            File(parentFolder, DB_NAME)
        }
        return JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}").also { driver ->
            Database.Schema.create(driver = driver)
        }
    }
}
