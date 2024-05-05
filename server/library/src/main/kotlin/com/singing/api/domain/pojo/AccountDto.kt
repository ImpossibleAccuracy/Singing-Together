package com.singing.api.domain.pojo

import com.singing.api.domain.model.Account

data class AccountDto(
    var id: Int? = null,
    var username: String? = null,
) {
    companion object {
        fun fromModel(model: Account) =
            AccountDto(
                id = model.id,
                username = model.username,
            )
    }
}
