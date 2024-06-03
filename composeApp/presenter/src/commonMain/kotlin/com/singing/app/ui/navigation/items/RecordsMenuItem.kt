package com.singing.app.ui.navigation.items

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.model.NavigationItem
import com.singing.app.presenter.generated.resources.Res
import com.singing.app.presenter.generated.resources.baseline_album_24
import com.singing.app.presenter.generated.resources.title_records
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

val recordsMenuItem = NavigationItem(
    title = {
        Text(text = stringResource(Res.string.title_records))
    },
    icon = {
        Icon(
            imageVector = vectorResource(Res.drawable.baseline_album_24),
            contentDescription = "",
        )
    },
    reference = { SharedScreen.RecordList() }
)
