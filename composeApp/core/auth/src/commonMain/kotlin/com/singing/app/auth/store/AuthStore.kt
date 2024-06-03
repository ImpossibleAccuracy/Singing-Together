package com.singing.app.auth.store

interface AuthStore {
    var authStoreData: AuthStoreData?

    fun clear()
}