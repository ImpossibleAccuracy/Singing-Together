package com.singing.app.common.views.base.publication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.singing.app.common.views.base.account.UserAvatar
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.model.state.PublicationUiData
import com.singing.app.ui.plus
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.listSpacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BasePublicationCard(
    modifier: Modifier,
    data: PublicationUiData,
    actions: PublicationCardActions,
    slotAfterAuthor: @Composable RowScope.() -> Unit,
    slotAfterDescription: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .plus(actions.onAuthorClick != null) {
                        background(MaterialTheme.colorScheme.surfaceContainer)
                    }
                    .clickable(enabled = actions.onAuthorClick != null) {
                        actions.onAuthorClick?.invoke()
                    }
                    .plus(actions.onAuthorClick != null) {
                        padding(
                            horizontal = MaterialTheme.dimens.dimen1,
                            vertical = MaterialTheme.dimens.dimen0_5,
                        )
                    }
            ) {
                UserAvatar(avatar = data.author.avatar)

                Spacer(Modifier.width(MaterialTheme.dimens.dimen1))

                Column {
                    Text(
                        text = data.author.username,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        text = data.createdAt,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            slotAfterAuthor()
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = data.description,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
        )

        FlowRow(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.listSpacing),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.listSpacing),
        ) {
            data.tags.forEach {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        slotAfterDescription()
    }
}