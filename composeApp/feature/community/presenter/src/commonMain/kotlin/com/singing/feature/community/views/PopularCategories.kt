package com.singing.feature.community.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.sp
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.list.Loader
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.utils.cardAppearance
import com.singing.feature.community.presenter.generated.resources.Res
import com.singing.feature.community.presenter.generated.resources.baseline_navigate_next_24
import com.singing.feature.community.presenter.generated.resources.common_error_subtitle
import com.singing.feature.community.presenter.generated.resources.common_error_title
import com.singing.feature.community.presenter.generated.resources.empty_popular_tags_subtitle
import com.singing.feature.community.presenter.generated.resources.empty_popular_tags_title
import com.singing.feature.community.presenter.generated.resources.label_for_last_day
import com.singing.feature.community.presenter.generated.resources.label_publications_count
import com.singing.feature.community.presenter.generated.resources.title_popular_categories
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun PopularCategories(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    categories: DataState<PersistentList<PublicationTagStatistics>>,
) {
    Column(
        modifier = modifier.cardAppearance(
            border = BorderStroke(
                MaterialTheme.dimens.bordersThickness,
                MaterialTheme.colorScheme.outlineVariant
            ),
            shape = shape,
            background = MaterialTheme.colorScheme.surfaceContainerLow,
            padding = PaddingValues(MaterialTheme.dimens.dimen2)
        ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
    ) {
        CategoriesHeader()

        LazyColumn(modifier = listModifier) {
            when (categories) {
                DataState.Loading -> {
                    item {
                        Loader(color = MaterialTheme.colorScheme.primary)
                    }
                }

                is DataState.Error -> {
                    item {
                        EmptyView(
                            title = stringResource(Res.string.common_error_title),
                            subtitle = stringResource(Res.string.common_error_subtitle),
                        )
                    }
                }

                is DataState.Empty -> {
                    item {
                        EmptyView(
                            title = stringResource(Res.string.empty_popular_tags_title),
                            subtitle = stringResource(Res.string.empty_popular_tags_subtitle),
                        )
                    }
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
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5),
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
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
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
                    //TODO: open publications list
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
