package com.singing.feature.record.list.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.list.Loader
import com.singing.app.common.views.base.record.RecordCard
import com.singing.app.common.views.toRecordCardData
import com.singing.app.domain.model.RecordData
import com.singing.app.ui.screen.dimens
import com.singing.feature.list.record.presenter.generated.resources.*
import com.singing.feature.list.record.presenter.generated.resources.Res
import com.singing.feature.list.record.presenter.generated.resources.common_error_subtitle
import com.singing.feature.list.record.presenter.generated.resources.common_error_title
import com.singing.feature.list.record.presenter.generated.resources.title_empty_records
import org.jetbrains.compose.resources.stringResource


@Composable
fun RecordsList(
    modifier: Modifier = Modifier,
    records: LazyPagingItems<RecordData>,
    highlightSelected: Boolean = true,
    selectedRecord: RecordData?,
    onSelectedRecordChange: (RecordData) -> Unit,
) {
    with(records.loadState) {
        when {
            refresh is LoadState.Loading -> {
                Loader(modifier)
            }

            refresh is LoadState.Error || append is LoadState.Error -> {
                EmptyView(
                    title = stringResource(Res.string.common_error_title),
                    subtitle = stringResource(Res.string.common_error_subtitle),
                )
            }

            refresh is LoadState.NotLoading && records.itemCount < 1 -> {
                EmptyView(
                    title = stringResource(Res.string.title_empty_records),
                    subtitle = stringResource(Res.string.subtitle_empty_records),
                )
            }

            refresh is LoadState.NotLoading -> {
                LazyColumn(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5),
                ) {
                    items(
                        count = records.itemCount,
                        key = records.itemKey { it.key },
                        contentType = records.itemContentType(),
                    ) { index ->
                        val item = records[index]!!

                        val isSelected = selectedRecord == item

                        val containerColor = when (isSelected && highlightSelected) {
                            true -> MaterialTheme.colorScheme.secondaryContainer
                            false -> Color.Transparent
                        }

                        val accuracyContainerColor = when (isSelected && highlightSelected) {
                            true -> MaterialTheme.colorScheme.surface
                            false -> MaterialTheme.colorScheme.surfaceContainerHigh
                        }

                        RecordCard(
                            data = item.toRecordCardData(),
                            containerColor = containerColor,
                            accuracyContainerColor = accuracyContainerColor,
                            onClick = {
                                onSelectedRecordChange(item)
                            },
                        )
                    }

                    if (append is LoadState.Loading) {
                        item {
                            Loader(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = MaterialTheme.dimens.dimen3,
                                        vertical = MaterialTheme.dimens.dimen2
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}
