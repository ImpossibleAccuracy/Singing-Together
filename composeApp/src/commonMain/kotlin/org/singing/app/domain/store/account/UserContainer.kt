package org.singing.app.domain.store.account

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.repository.account.AccountRepository

@Stable
object UserContainer {
    val user = MutableStateFlow<AccountUiData?>(
        AccountRepository.DefaultItems.first()
    )
}
