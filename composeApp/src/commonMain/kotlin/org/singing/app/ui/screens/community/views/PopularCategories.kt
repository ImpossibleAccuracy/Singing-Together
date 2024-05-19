package org.singing.app.ui.screens.community.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.singing.app.composeapp.generated.resources.*
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.CategoryInfo
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.cardAppearance
import org.singing.app.ui.views.base.Loader


@Composable
fun PopularCategories(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    categories: ImmutableList<CategoryInfo>,
    isCategoriesLoading: Boolean = true,
) {
    Column(
        modifier = modifier
            .cardAppearance(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = shape,
                background = MaterialTheme.colorScheme.surfaceContainerLow,
                padding = PaddingValues(16.dp)
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CategoriesHeader()

        when {
            isCategoriesLoading -> {
                Loader(color = MaterialTheme.colorScheme.primary)
            }

            categories.isEmpty() -> {
                // TODO: add EmptyView
            }

            else -> {
                CategoriesList(
                    listModifier = listModifier,
                    categories = categories
                )
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

@Composable
private fun CategoriesList(
    listModifier: Modifier,
    categories: ImmutableList<CategoryInfo>
) {
    LazyColumn(
        modifier = listModifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(categories.size) {
            val item = categories[it]

            Row(
                verticalAlignment = Alignment.CenterVertically,
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

                Space(12.dp)

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
}
