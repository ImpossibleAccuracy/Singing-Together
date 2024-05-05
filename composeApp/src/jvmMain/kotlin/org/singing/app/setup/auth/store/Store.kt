package org.singing.app.setup.auth.store

import com.russhwolf.settings.PreferencesSettings
import java.util.prefs.Preferences

private val delegate: Preferences = Preferences.userRoot().node("singing-together")

val authSettings by lazy {
    PreferencesSettings(delegate)
}
