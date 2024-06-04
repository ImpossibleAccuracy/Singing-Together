package com.singing.app.ui.navigation.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.domain.model.UserData
import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.model.NavigationItem
import com.singing.app.presenter.generated.resources.Res
import com.singing.app.presenter.generated.resources.baseline_person_24
import com.singing.app.presenter.generated.resources.title_profile
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

internal fun buildProfileMenuItem(user: UserData?) = when (user) {
    null -> NavigationItem(
        title = {
            Text(text = stringResource(Res.string.title_profile))
        },
        icon = {
            Icon(
                imageVector = vectorResource(Res.drawable.baseline_person_24),
                contentDescription = ""
            )
        },
        reference = { SharedScreen.Auth }
    )

    else ->
        NavigationItem(
            title = {
                Text(text = stringResource(Res.string.title_profile))
            },
            icon = {
                if (user.avatar == null) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.baseline_person_24),
                        contentDescription = ""
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(MaterialTheme.dimens.icon)
                            .clip(RoundedCornerShape(50)),
                        painter = rememberImagePainter(user.avatar!!),
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                    )
                }
            },
            reference = { SharedScreen.UserProfile(user) }
        )
}
