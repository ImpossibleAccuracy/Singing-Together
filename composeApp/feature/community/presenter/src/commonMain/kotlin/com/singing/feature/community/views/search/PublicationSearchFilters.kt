package com.singing.feature.community.views.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.ChipTextFieldState
import com.singing.app.common.views.base.AppChipTextField
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.utils.cardAppearance
import com.singing.domain.model.PublicationSort
import com.singing.feature.community.model.PublicationsSearchFilters
import com.singing.feature.community.presenter.generated.resources.*
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource


@Composable
@ReadOnlyComposable
private fun Modifier.searchChipAppearance(
    onClick: () -> Unit,
    padding: PaddingValues,
) = height(40.dp)
    .cardAppearance(
        border = BorderStroke(MaterialTheme.dimens.bordersThickness, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.small,
        background = MaterialTheme.colorScheme.surfaceContainerLow,
        padding = padding,
        onClick = onClick,
    )


data class PublicationSearchData(
    val currentTagText: String,
    val tags: ChipTextFieldState<Chip>,
    val showUserPublications: Boolean,
    val sortType: PublicationSort,
    val description: String,
)

data class PublicationSearchActions(
    val onTagTextUpdated: (String) -> Unit,
    val onTagSubmit: (String) -> Chip?,
    val onTagRemove: (Chip) -> Unit,
    val onShowUserPublicationsChanged: (Boolean) -> Unit,
    val onSortTypeChanged: (PublicationSort) -> Unit,
    val onDescriptionUpdated: (String) -> Unit,
)

@Composable
fun PublicationSearchFiltersContainer(
    modifier: Modifier = Modifier,
    searchFilters: PublicationsSearchFilters,
    onFiltersUpdate: (PublicationsSearchFilters) -> Unit,
) {
    val tags = remember(searchFilters.tags) {
        ChipTextFieldState(searchFilters.tags.map(::Chip))
    }

    PublicationSearchFilters(
        modifier = modifier,
        data = PublicationSearchData(
            currentTagText = searchFilters.currentTagText,
            tags = tags,
            showUserPublications = searchFilters.showOwnPublications,
            sortType = searchFilters.sort,
            description = searchFilters.description,
        ),
        actions = PublicationSearchActions(
            onTagTextUpdated = {
                onFiltersUpdate(
                    searchFilters.copy(
                        currentTagText = it
                    )
                )
            },
            onTagSubmit = {
                onFiltersUpdate(
                    searchFilters.copy(
                        currentTagText = "",
                        tags = searchFilters.tags
                            .plus(it)
                            .toImmutableList()
                    )
                )

                Chip(it)
            },
            onTagRemove = { chip ->
                onFiltersUpdate(
                    searchFilters.copy(
                        tags = searchFilters.tags
                            .filter { it != chip.text }
                            .toImmutableList()
                    )
                )

                tags.removeChip(chip)
            },
            onShowUserPublicationsChanged = {
                onFiltersUpdate(
                    searchFilters.copy(
                        showOwnPublications = it
                    )
                )
            },
            onSortTypeChanged = {
                onFiltersUpdate(
                    searchFilters.copy(
                        sort = it
                    )
                )
            },
            onDescriptionUpdated = {
                onFiltersUpdate(
                    searchFilters.copy(
                        description = it
                    )
                )
            }
        )
    )
}

@Composable
fun PublicationSearchFilters(
    modifier: Modifier = Modifier,
    data: PublicationSearchData,
    actions: PublicationSearchActions,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5)
    ) {
        Text(
            text = stringResource(Res.string.title_search_publications),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
            ) {
                AppChipTextField(
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    readOnlyChips = true,
                    label = stringResource(Res.string.label_search_by_tags),
                    state = data.tags,
                    value = data.currentTagText,
                    onValueChange = actions.onTagTextUpdated,
                    onSubmit = actions.onTagSubmit,
                    onRemove = actions.onTagRemove,
                )

                Row(
                    modifier = Modifier.padding(top = MaterialTheme.dimens.dimen2),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
                ) {
                    ShowUserPublicationsChip(
                        value = data.showUserPublications,
                        onValueChanged = actions.onShowUserPublicationsChanged,
                    )

                    Box {
                        var expanded by remember { mutableStateOf(false) }

                        SortChip(
                            sortType = data.sortType,
                            onClick = {
                                expanded = true
                            },
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            PublicationSort.entries.forEach {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = getSortTypeText(it))
                                    },
                                    onClick = {
                                        actions.onSortTypeChanged(it)

                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                ),
                value = data.description,
                onValueChange = actions.onDescriptionUpdated,
                label = {
                    Text(
                        text = stringResource(Res.string.label_search_by_description),
                        lineHeight = 1.5.em,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
            )
        }
    }
}

@Composable
private fun ShowUserPublicationsChip(
    value: Boolean,
    onValueChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.searchChipAppearance(
            padding = PaddingValues(
                start = MaterialTheme.dimens.dimen2,
                end = MaterialTheme.dimens.dimen2,
            ),
            onClick = {
                onValueChanged(!value)
            },
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
    ) {
        Checkbox(
            modifier = Modifier.size(MaterialTheme.dimens.dimen3),
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
            ),
            checked = value,
            onCheckedChange = onValueChanged,
        )

        Text(
            text = stringResource(Res.string.action_show_my_publications),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun SortChip(
    sortType: PublicationSort,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .widthIn(min = 224.dp)
            .searchChipAppearance(
                padding = PaddingValues(
                    start = MaterialTheme.dimens.dimen2,
                    end = MaterialTheme.dimens.dimen2,
                ),
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5, Alignment.CenterHorizontally),
    ) {
        Text(
            text = stringResource(
                resource = Res.string.label_publication_sort,
                getSortTypeText(sortType).lowercase()
            ),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
        )

        Icon(
            modifier = Modifier.size(MaterialTheme.dimens.dimen3),
            imageVector = Icons.Default.ArrowDropDown,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = "",
        )
    }
}


@Composable
private fun getSortTypeText(sortType: PublicationSort) =
    when (sortType) {
        PublicationSort.DateCreated -> stringResource(Res.string.label_sort_publication_date)
        PublicationSort.Accuracy -> stringResource(Res.string.label_sort_accuracy)
        PublicationSort.Duration -> stringResource(Res.string.label_sort_duration)
    }
