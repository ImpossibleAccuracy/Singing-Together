package org.singing.app.domain.store.account

import kotlinx.coroutines.flow.MutableStateFlow
import org.singing.app.domain.model.AccountUiData

object UserContainer {
    val user = MutableStateFlow<AccountUiData?>(
        AccountUiData(
            username = "Admin",
            avatar = "https://storage.googleapis.com/pod_public/1300/151089.jpg"
        )
    )
}
