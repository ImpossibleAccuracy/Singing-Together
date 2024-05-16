package org.singing.app.ui.screens.record.list.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.ui.base.Divider
import org.singing.app.ui.screens.record.list.RecordListViewModel
import org.singing.app.ui.screens.record.list.model.RecordOperationTab
import org.singing.app.ui.screens.record.list.tabs.*


data class RecordOperationTabData<T>(
    val name: RecordOperationTab,
    val label: @Composable () -> String,
    val enabled: ((record: RecordData, viewModel: RecordListViewModel) -> Boolean)? = null,
    val buildState: (record: RecordData, viewModel: RecordListViewModel) -> T,
    val content: @Composable (viewModel: RecordListViewModel, state: T, updateState: (T) -> Unit) -> Unit,
)

private fun createTabs(): List<RecordOperationTabData<Any>> = listOf(
    RecordOperationTabData(
        name = RecordOperationTab.Record,
        label = { "Record" },
        buildState = { record, _ ->
            RecordTabData(
                record = record,
            )
        },
        content = { viewModel, s, updateState ->
            val state = s as RecordTabData

            RecordTab(
                data = state,
                viewModel = viewModel,
                onDataUpdate = updateState,
            )
        }
    ),
    RecordOperationTabData(
        name = RecordOperationTab.Player,
        label = { "Player" },
        buildState = { record, viewModel ->
            viewModel.resetRecordPlayer()

            PlayerTabData(
                player = viewModel.recordPlayer,
                record = record,
            )
        },
        content = { _, s, _ ->
            val state = s as PlayerTabData

            PlayerTab(
                data = state,
            )
        }
    ),
    RecordOperationTabData(
        name = RecordOperationTab.Publication,
        label = { "Publication" },
        enabled = { record, _ ->
            !record.isSavedRemote && UserContainer.user.value != null
        },
        buildState = { record, _ ->
            PublicationTabData(
                record = record,
                description = "",
            )
        },
        content = { viewModel, s, updateState ->
            val state = s as PublicationTabData

            PublicationTab(
                viewModel = viewModel,
                data = state,
                onDataUpdate = updateState,
            )
        }
    ),
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordOperationsTabs(
    viewModel: RecordListViewModel,
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    record: RecordData,
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(record) {
        pagerState.scrollToPage(0)
    }

    val tabs = remember {
        createTabs()
    }

    val initialTabStates = remember(record) {
        tabs.map { it.buildState(record, viewModel) }
    }

    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        val mutableTabData = remember(initialTabStates) {
            mutableStateListOf(*initialTabStates.toTypedArray())
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = MaterialTheme.colorScheme.tertiary,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = MaterialTheme.colorScheme.tertiary,
                )
            },
        ) {
            tabs.forEachIndexed { index, tab: RecordOperationTabData<*> ->
                val isEnabled = tab.enabled?.let { remember(record) { it(record, viewModel) } } ?: true

                Tab(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = isEnabled,
                    selectedContentColor = MaterialTheme.colorScheme.tertiary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(
                        text = tab.label(),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        Divider()

        HorizontalPager(
            state = pagerState,
        ) { currentTab ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 16.dp
                    )
            ) {
                tabs[currentTab].content(
                    viewModel = viewModel,
                    state = mutableTabData[currentTab],
                    updateState = {
                        mutableTabData[currentTab] = it
                    }
                )
            }
        }
    }
}