package org.singing.app.setup.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.singing.app.Database
import java.io.File

actual class DatabaseDriverFactory {
    companion object {
        private const val DbPath = "MyApp"
        private const val DbName = "mylocaldb"
    }

    actual fun createDriver(): SqlDriver {
        val isDebug = true

        val parentFolder = if (isDebug) {
            File(System.getProperty("java.io.tmpdir"))
        } else {
            File(System.getProperty("user.home") + DbPath)
        }

        if (!parentFolder.exists()) {
            parentFolder.mkdirs()
        }
        val databasePath = if (isDebug) {
            File(System.getProperty("java.io.tmpdir"), DbName)
        } else {
            File(parentFolder, DbName)
        }
        return JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}").also { driver ->
            Database.Schema.create(driver = driver)
        }
    }
}
