package org.singing.app.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ContentContainer(content: @Composable() (BoxScope.() -> Unit)) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
        content = {
            Box(
                modifier = Modifier
                    .widthIn(max = 1200.dp)
                    .fillMaxSize(),
                content = content,
            )
        },
    )
}