package org.singing.app.domain.store.account

import kotlinx.coroutines.flow.MutableStateFlow
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.repository.account.AccountRepository

object UserContainer {
    val user = MutableStateFlow<AccountUiData?>(
        AccountRepository.DefaultItems.first()
    )
}
