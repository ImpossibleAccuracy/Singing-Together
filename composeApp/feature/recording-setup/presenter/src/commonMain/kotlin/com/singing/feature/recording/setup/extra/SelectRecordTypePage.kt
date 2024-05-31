package com.singing.feature.recording.setup.extra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.navigation.base.BaseAppPage

class SelectRecordTypePage : BaseAppPage {
    @Composable
    override fun Content() {
        SelectRecordTypeScreen(
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        bottomStart = 24.dp,
                    )
                )
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
    }
}
