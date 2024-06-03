package com.singing.app.ui.navigation.items

import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.domain.model.UserData
import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.model.NavigationItem
import com.singing.app.presenter.generated.resources.Res
import com.singing.app.presenter.generated.resources.baseline_person_24
import com.singing.app.presenter.generated.resources.title_profile
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
                        painter = rememberImagePainter(user.avatar!!),
                        contentDescription = "",
                    )
                }
            },
            reference = { SharedScreen.UserProfile(user) }
        )
}
