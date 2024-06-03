package com.singing.feature.account.profile.viewmodel

sealed interface AccountProfileIntent {
    data object Logout : AccountProfileIntent
}