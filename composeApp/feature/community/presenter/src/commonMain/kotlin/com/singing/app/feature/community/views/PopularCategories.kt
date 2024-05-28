package com.singing.app.feature.community.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.common.views.base.list.Loader
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.ui.utils.cardAppearance
import com.singing.feature.community.presenter.generated.resources.*
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun PopularCategories(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    categories: DataState<PersistentList<PublicationTagStatistics>>,
) {
    Column(
        modifier = modifier.cardAppearance(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = shape,
            background = MaterialTheme.colorScheme.surfaceContainerLow,
            padding = PaddingValues(16.dp)
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CategoriesHeader()

        LazyColumn {
            when (categories) {
                DataState.Loading -> {
                    item {
                        Loader(color = MaterialTheme.colorScheme.primary)
                    }
                }

                is DataState.Error -> {
                    // TODO: add ErrorView
                }

                is DataState.Empty -> {
                    // TODO: add EmptyView
                }

                is DataState.Success -> {
                    categoriesList(
                        categories = categories.data
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoriesHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(Res.string.title_popular_categories),
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
            )
        )

        Text(
            text = stringResource(Res.string.label_for_last_day),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

private fun LazyListScope.categoriesList(
    categories: PersistentList<PublicationTagStatistics>
) {
    items(
        items = categories,
        key = { it.title },
        contentType = { PublicationTagStatistics::class.simpleName }
    ) { item ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier.weight(weight = 1f),
            ) {
                Text(
                    text = item.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = stringResource(Res.string.label_publications_count, item.publications),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                )
            }

            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.baseline_navigate_next_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "",
                )
            }
        }
    }
}
