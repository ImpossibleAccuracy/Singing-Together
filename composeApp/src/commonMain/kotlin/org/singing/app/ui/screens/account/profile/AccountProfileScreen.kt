package org.singing.app.ui.screens.account.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.connectVerticalNestedScroll
import org.singing.app.ui.screens.account.profile.views.Banner
import org.singing.app.ui.screens.account.profile.views.Publications

class AccountProfileScreen : AppScreen() {
    @Composable
    override fun Content() {
        val verticalScroll = rememberScrollState()

        ContentContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        state = verticalScroll,
                    )
                    .padding(
                        top = 16.dp,
                        bottom = 24.dp,
                    ),
            ) {
                Banner(
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Space(24.dp)

                Publications(
                    modifier = Modifier
                        .connectVerticalNestedScroll(10000.dp, verticalScroll)
                )
            }
        }
    }
}
