package com.singing.app.auth.store

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValueOrNull
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)
class AuthStoreImpl : AuthStore {
    companion object {
        private const val KEY = "AUTH_DATA"
    }

    private val settings: Settings by lazy { Settings() }

    override var authStoreData: AuthStoreData?
        get() = settings.decodeValueOrNull(AuthStoreData.serializer(), KEY)
        set(value) {
            if (value == null) {
                settings.remove(KEY)
            } else {
                settings.encodeValue(AuthStoreData.serializer(), KEY, value)
            }
        }

    override fun clear() {
        settings.clear()
    }
}
