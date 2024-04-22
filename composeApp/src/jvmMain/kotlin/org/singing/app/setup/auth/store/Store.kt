package org.singing.app.setup.auth.store

import com.russhwolf.settings.PreferencesSettings
import java.util.prefs.Preferences

val delegate = Preferences.userRoot().node("singing-together")

val authSettings by lazy {
    PreferencesSettings(delegate)
}
