package com.singing.app.ui.navigation.items

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.model.NavigationItem
import com.singing.app.presenter.generated.resources.Res
import com.singing.app.presenter.generated.resources.baseline_explore_24
import com.singing.app.presenter.generated.resources.title_community
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

val communityMenuItem = NavigationItem(
    title = {
        Text(text = stringResource(Res.string.title_community))
    },
    icon = {
        Icon(
            imageVector = vectorResource(Res.drawable.baseline_explore_24),
            contentDescription = "",
        )
    },
    reference = { SharedScreen.Community }
)
