package org.singing.app.ui.screens.community.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_navigate_next_24
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.CategoryInfo
import org.singing.app.ui.base.Space


@Composable
fun PopularCategories(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    categories: ImmutableList<CategoryInfo>,
    isCategoriesLoading: Boolean = true,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Popular categories",
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                )
            )

            Space(4.dp)

            Text(
                text = "For the last day",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
            )
        }

        if (isCategoriesLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        } else if (categories.isEmpty()) {
            // TODO: add EmptyView
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = listModifier.fillMaxWidth(),
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
                                text = "${item.publications} publications",
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
    }
}
