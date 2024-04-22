package org.singing.app.ui.navigation

import cafe.adriel.voyager.core.screen.Screen
import org.singing.app.ui.screens.record.RecordScreen

actual fun getStartDestination(): Screen =
    RecordScreen()
