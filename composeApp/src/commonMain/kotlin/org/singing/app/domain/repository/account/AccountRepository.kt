package org.singing.app.domain.repository.account

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.singing.app.domain.model.AccountInfo
import org.singing.app.domain.model.AccountUiData
import kotlin.time.Duration.Companion.days

class AccountRepository {
    companion object {
        val DefaultItems = listOf(
            AccountUiData(
                id = 1,
                username = "Admin",
                avatar = "https://storage.googleapis.com/pod_public/1300/151089.jpg"
            ),
            AccountUiData(
                id = 2,
                username = "User1",
                avatar = "https://d.newsweek.com/en/full/1979380/dog-running-through-autumn-leaves.jpg"
            ),
            AccountUiData(
                id = 3,
                username = "User2",
                avatar = "https://ae04.alicdn.com/kf/S18e92125b5de4c10807acb3ae6b5517ce.jpg_480x480.jpg"
            )
        )
    }

    suspend fun findAccount(id: Int): AccountUiData? =
        withContext(Dispatchers.IO) {
            DefaultItems.find { it.id == id }
        }

    suspend fun getAccountInfo(accountId: Int): AccountInfo =
        withContext(Dispatchers.IO) {
            AccountInfo(
                publicationsCount = 10,
                registeredAt = Clock.System.now() - 23.days
            )
        }
}
