package org.singing.app.ui.base

import cafe.adriel.voyager.core.screen.Screen

abstract class AppScreen : Screen {
    open fun onLeave() {}

    open fun onClose() {}
}

