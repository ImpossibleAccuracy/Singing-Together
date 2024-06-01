package com.singing.app.common.views.base.publication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.base.account.UserAvatar
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.model.state.PublicationUiData
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
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier
                    .height(36.dp)
                    .clip(shape = RoundedCornerShape(50))
                    .clickable(enabled = actions.onAuthorClick != null) {
                        actions.onAuthorClick?.invoke()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UserAvatar(
                    size = 36.dp,
                    modifier = Modifier.padding(MaterialTheme.dimens.dimen0_5),
                    avatar = data.author.avatar
                )

                Column(Modifier.padding(horizontal = MaterialTheme.dimens.dimen1_5)) {
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