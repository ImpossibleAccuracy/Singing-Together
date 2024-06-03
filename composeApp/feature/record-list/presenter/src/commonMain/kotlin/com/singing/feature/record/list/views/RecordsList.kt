package com.singing.feature.record.list.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.singing.app.common.views.base.AppFilledButton
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.list.Loader
import com.singing.app.common.views.base.record.RecordCard
import com.singing.app.common.views.base.record.recordCardAppearance
import com.singing.app.common.views.toRecordCardData
import com.singing.app.domain.model.RecordData
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.utils.cardAppearance
import com.singing.feature.list.record.presenter.generated.resources.*
import org.jetbrains.compose.resources.stringResource


@Composable
fun RecordsList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    records: LazyPagingItems<RecordData>,
    onSelectedRecordChange: (RecordData) -> Unit,
    navigateRecording: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        with(records.loadState) {
            when {
                refresh is LoadState.Loading -> {
                    Loader()
                }

                refresh is LoadState.Error || append is LoadState.Error -> {
                    EmptyView(
                        modifier = Modifier.padding(contentPadding),
                        title = stringResource(Res.string.common_error_title),
                        subtitle = stringResource(Res.string.common_error_subtitle),
                    )
                }

                refresh is LoadState.NotLoading && records.itemCount < 1 -> {
                    EmptyView(
                        modifier = Modifier
                            .widthIn(min = 400.dp, max = 600.dp)
                            .cardAppearance(
                                shape = MaterialTheme.shapes.medium,
                                background = MaterialTheme.colorScheme.primaryContainer,
                                padding = PaddingValues(
                                    horizontal = MaterialTheme.dimens.dimen5,
                                    vertical = MaterialTheme.dimens.dimen3,
                                ),
                            ),
                        title = stringResource(Res.string.title_empty_records),
                        subtitle = stringResource(Res.string.subtitle_empty_records),
                        action = {
                            AppFilledButton(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                label = stringResource(Res.string.action_start_record),
                                onClick = navigateRecording,
                            )
                        }
                    )
                }

                refresh is LoadState.NotLoading -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5),
                    ) {
                        items(
                            count = records.itemCount,
                            key = records.itemKey { it.key },
                            contentType = records.itemContentType(),
                        ) { index ->
                            val item = records[index]!!

                            RecordCard(
                                modifier = Modifier.recordCardAppearance(
                                    showBorder = false,
                                    containerColor = Color.Transparent,
                                    onClick = {
                                        onSelectedRecordChange(item)
                                    },
                                ),
                                data = item.toRecordCardData(),
                                accuracyContainerColor = MaterialTheme.colorScheme.surfaceContainer,
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
}
