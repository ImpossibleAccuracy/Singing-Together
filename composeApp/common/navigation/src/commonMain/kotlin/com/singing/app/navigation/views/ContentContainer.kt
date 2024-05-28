package com.singing.app.navigation.views

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// TODO
val DefaultPagePaddings = PaddingValues(
    top = 44.dp,
    bottom = 24.dp,
    start = 12.dp,
    end = 12.dp,
)

@Composable
fun ContentContainer(content: @Composable (BoxScope.() -> Unit)) {
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
