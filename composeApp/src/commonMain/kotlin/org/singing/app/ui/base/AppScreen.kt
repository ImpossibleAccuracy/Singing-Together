package org.singing.app.ui.base

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.screen.Screen
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

@Stable
abstract class AppScreen : Screen, KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }

    open fun onLeave() {}

    open fun onClose() {}
}

