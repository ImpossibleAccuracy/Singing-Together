package com.singing.feature.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.singing.app.domain.model.Publication
import com.singing.app.navigation.base.AppPage
import com.singing.app.navigation.base.screenModel
import com.singing.app.navigation.views.ContentContainer
import com.singing.app.navigation.views.DefaultPagePaddings

class PublicationDetailsPage(
    val publication: Publication,
) : AppPage<PublicationDetailsViewModel>() {
    @Composable
    override fun rememberLocalModelFactory(): PublicationDetailsViewModel {
        return screenModel<PublicationDetailsViewModel>(publication)
    }

    @Composable
    override fun Content(screenModel: PublicationDetailsViewModel) {
        ContentContainer {
            val uiState by screenModel.uiState.collectAsState()

            PublicationDetailsScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DefaultPagePaddings),
                viewModel = screenModel,
                uiState = uiState,
            )
        }
    }
}
