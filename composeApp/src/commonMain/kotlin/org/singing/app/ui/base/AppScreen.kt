package org.singing.app.ui.base

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen

abstract class AppScreen : Screen {
    open fun onClose() {}

    @Composable
    fun ContentContainer(content: @Composable (BoxScope.() -> Unit)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize(),
            content = {
                Box(
                    modifier = Modifier
                        .widthIn(max = 1200.dp)
                        .fillMaxWidth(),
                    content = content,
                )
            },
        )
    }
}
