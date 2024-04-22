package org.singing.app.setup.database

import com.singing.app.Database

class AppDatabase(driverFactory: DatabaseDriverFactory) {
    private val database by lazy { createDatabase(driverFactory) }

    fun createDatabase(driverFactory: DatabaseDriverFactory): Database {
        val driver = driverFactory.createDriver()
        return Database(driver)
    }
}
