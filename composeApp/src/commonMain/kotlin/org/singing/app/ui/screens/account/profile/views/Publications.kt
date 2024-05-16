package org.singing.app.ui.screens.account.profile.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Publications(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        columns = GridCells.Adaptive(
            minSize = 380.dp,
        ),
    ) {
        items(5) {
//            PublicationCard(
//                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
//            )
        }
    }
}
