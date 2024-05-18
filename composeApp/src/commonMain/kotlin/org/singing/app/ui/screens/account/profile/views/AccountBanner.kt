package org.singing.app.ui.screens.account.profile.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_person_24
import kotlinx.datetime.Clock
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.painterResource
import org.singing.app.domain.model.AccountInfo
import org.singing.app.domain.model.AccountUiData
import org.singing.app.ui.base.Space


@Composable
fun AccountBanner(
    modifier: Modifier = Modifier,
    account: AccountUiData,
    accountInfo: AccountInfo?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .size(size = 128.dp)
                .clip(shape = RoundedCornerShape(50))
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = when (account.avatar) {
                    null -> painterResource(Res.drawable.baseline_person_24)
                    else -> rememberImagePainter(account.avatar)
                },
                contentScale = ContentScale.Crop,
                contentDescription = "Avatar",
            )
        }

        Space(24.dp)

        Column {
            Text(
                text = account.username,
                color = MaterialTheme.colorScheme.onBackground,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            )

            Space(8.dp)

            Column {
                if (accountInfo == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    Text(
                        text = "${accountInfo.publicationsCount} publications",
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 1.43.em,
                        style = MaterialTheme.typography.labelLarge
                    )

                    Text(
                        text = "Registered for ${
                            HumanReadable.duration(Clock.System.now() - accountInfo.registeredAt.instant)
                        }",
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 1.43.em,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
