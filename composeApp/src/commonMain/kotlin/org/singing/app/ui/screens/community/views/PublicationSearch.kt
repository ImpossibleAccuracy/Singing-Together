package org.singing.app.ui.screens.community.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.ChipTextFieldState
import org.singing.app.domain.model.PublicationSort
import org.singing.app.ui.base.Space
import org.singing.app.ui.views.base.AppChipTextField


private val SearchChipModifier
    @Composable
    get() = Modifier
        .height(40.dp)
        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.small)
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.surfaceContainerLow)


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


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PublicationSearchFilters(
    modifier: Modifier = Modifier,
    data: PublicationSearchData,
    actions: PublicationSearchActions,
) {
    Column(modifier = modifier) {
        Text(
            text = "Search publications",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
        )

        Space(12.dp)

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppChipTextField(
                modifier = Modifier
                    .weight(1f)
                    .width(720.dp),
                shape = MaterialTheme.shapes.medium,
                borderColor = MaterialTheme.colorScheme.outlineVariant,
                readOnlyChips = true,
                label = "Search by tags",
                state = data.tags,
                value = data.currentTagText,
                onValueChange = actions.onTagTextUpdated,
                onSubmit = actions.onTagSubmit,
                onRemove = actions.onTagRemove,
            )

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
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

        Space(8.dp)

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
                    text = "Search by description",
                    lineHeight = 1.5.em,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
        )
    }
}

@Composable
private fun ShowUserPublicationsChip(
    value: Boolean,
    onValueChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = SearchChipModifier
            .clickable {
                onValueChanged(!value)
            }
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            modifier = Modifier.size(24.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
            ),
            checked = value,
            onCheckedChange = onValueChanged,
        )

        Space(12.dp)

        Text(
            text = "Show my publications",
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
        modifier = SearchChipModifier
            .widthIn(min = 224.dp)
            .clickable(onClick = onClick)
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Sort by ${getSortTypeText(sortType).lowercase()}",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
        )

        Space(12.dp)

        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Default.ArrowDropDown,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = "",
        )
    }
}


@Composable
private fun getSortTypeText(sortType: PublicationSort) =
    when (sortType) {
        PublicationSort.DateCreated -> "Publication Date"
        PublicationSort.Accuracy -> "Accuracy"
        PublicationSort.Duration -> "Duration"
    }
