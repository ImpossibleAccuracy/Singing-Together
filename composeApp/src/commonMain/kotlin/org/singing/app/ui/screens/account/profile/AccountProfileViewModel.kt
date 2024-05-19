package org.singing.app.ui.screens.account.profile

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.singing.app.domain.model.AccountInfo
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.Publication
import org.singing.app.domain.repository.account.AccountRepository
import org.singing.app.domain.repository.publication.PublicationRepository
import org.singing.app.ui.base.AppViewModel

@Stable
class AccountProfileViewModel(
    private val publicationRepository: PublicationRepository,
    private val accountRepository: AccountRepository,
) : AppViewModel() {
    private val _accountInfo = MutableStateFlow<AccountInfo?>(null)
    val accountInfo = _accountInfo.asStateFlow()

    private val _publications = MutableStateFlow<PersistentList<Publication>>(persistentListOf())
    val publication = _publications.asStateFlow()


    fun load(accountUiData: AccountUiData) {
        viewModelScope.launch {
            launch {
                _publications.value = publicationRepository
                    .getAccountPublications(accountId = accountUiData.id)
                    .toPersistentList()
            }

            launch {
                _accountInfo.value = accountRepository
                    .getAccountInfo(accountId = accountUiData.id)
            }
        }
    }
}
