package com.singing.api.security.builder.authentication

import com.singing.api.domain.model.Account
import com.singing.api.security.builder.authentication.AccountAuthentication.Companion.PRIVILEGE_PREFIX
import com.singing.api.security.builder.authentication.AccountAuthentication.Companion.ROLE_PREFIX

fun buildAuthorities(account: Account): List<String> =
    account.roles.let { roles ->
        roles.map { ROLE_PREFIX + it.title }
            .plus(
                roles
                    .flatMap { it.privileges }
                    .map { PRIVILEGE_PREFIX + it.title }
            )
    }
